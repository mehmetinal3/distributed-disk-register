package com.example.family;

import io.grpc.stub.StreamObserver;
import com.example.family.FamilyServiceGrpc.FamilyServiceImplBase;
// Yeni eklenen kütüphaneler (Yönlendirme yapmak için)
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList; // YENİ: Liste tutmak için
import java.util.List;      // YENİ: Liste arayüzü

/**
 * FamilyServiceImpl
 * GÖREVİ: Dışarıdan gelen "Kaydet (SET)" ve "Getir (GET)" isteklerini yapan sınıftır.
 * ARTIK CHAT YOK, VERİ DEPOLAMA, YÜK DENGELEME VE REPLICATION VAR.
 */
public class FamilyServiceImpl extends FamilyServiceImplBase {

    // Her çalışan terminalin (Node) kendi özel dosya ismi olsun.
    // Örn: storage_12345.txt
    private final String fileName = "storage_" + ProcessHandle.current().pid() + ".txt";

    /**
     * YARDIMCI METOT: tolerance.conf dosyasını okur.
     * replication=2 yazıyorsa 2 döner, dosya yoksa 1 döner.
     */
    private int getReplicationFactor() {
        File file = new File("tolerance.conf");
        if (!file.exists()) {
            return 1; // Dosya yoksa standart mod (Yedekleme yok)
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("replication=")) {
                    // "replication=2" -> "2" kısmını alıp sayıya çevir
                    return Integer.parseInt(line.split("=")[1].trim());
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Config okuma hatası, varsayılan 1 kullanılıyor.");
        }
        return 1;
    }

    /**
     * 1. JOIN (AĞA KATILMA)
     * Yeni gelen üyeyi karşılar ve listeye ekler.
     */
    @Override
    public void join(NodeInfo request, StreamObserver<JoinResponse> responseObserver) {
        String yeniGelen = request.getHost() + ":" + request.getPort();
        System.out.println("👋 [Lider] Yeni katılım isteği: " + yeniGelen);

        // Liderin hafızasına (Registry) ekle
        NodeRegistry.registerNode(yeniGelen);

        // Cevap dön
        JoinResponse response = JoinResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Aramıza hoşgeldin! Dosya ismin: " + fileName)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * 2. STORE MESSAGE (KAYDETME - LOAD BALANCING & REPLICATION)
     * BURASI GÜNCELLENDİ: Config dosyasına göre birden fazla yere gönderim yapar.
     */
    @Override
    public void storeMessage(StoreRequest request, StreamObserver<StoreResponse> responseObserver) {
        // Önce Registry'e bak: Yönlendirecek kimse var mı?
        // İlk hedefi çekiyoruz (Round Robin)
        String firstTarget = NodeRegistry.getNextNode();

        if (firstTarget != null) {
            // --- SENARYO A: LİDER MODU (YÖNLENDİRME & YEDEKLEME) ---
            
            // 1. Config dosyasından kaç kopya istendiğini öğren
            int replicationCount = getReplicationFactor();
            System.out.println("⚖️ [FaultTolerance] Hedef Kopya Sayısı: " + replicationCount);

            // Hedeflerin listesini oluştur
            List<String> targets = new ArrayList<>();
            targets.add(firstTarget); // İlk hedef cepte

            // Eğer 2. kopya isteniyorsa, sıradaki diğer elemanı da al
            for (int i = 1; i < replicationCount; i++) {
                String nextTarget = NodeRegistry.getNextNode();
                if (nextTarget != null) {
                    targets.add(nextTarget);
                }
            }

            StringBuilder resultMessage = new StringBuilder();
            boolean atLeastOneSuccess = false;

            // 2. Belirlenen tüm hedeflere sırayla gönder
            for (String targetNode : targets) {
                System.out.println("🔀 [Lider] Yönlendiriliyor -> " + targetNode);

                String[] parts = targetNode.split(":");
                ManagedChannel channel = ManagedChannelBuilder.forAddress(parts[0], Integer.parseInt(parts[1]))
                        .usePlaintext()
                        .build();
                
                try {
                    FamilyServiceGrpc.FamilyServiceBlockingStub stub = FamilyServiceGrpc.newBlockingStub(channel);
                    StoreResponse responseFromWorker = stub.storeMessage(request);
                    
                    if (responseFromWorker.getSuccess()) {
                        atLeastOneSuccess = true;
                        resultMessage.append("[").append(responseFromWorker.getMessage()).append("] ");
                    }
                } catch (Exception e) {
                    System.err.println("❌ Yönlendirme Hatası (" + targetNode + "): " + e.getMessage());
                } finally {
                    channel.shutdown();
                }
            }

            // Sonucu İstemciye Bildir
            if (atLeastOneSuccess) {
                responseObserver.onNext(StoreResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("İşlem Tamam: " + resultMessage.toString())
                        .build());
            } else {
                responseObserver.onNext(StoreResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Tüm kopyalamalar başarısız!")
                        .build());
            }

        } else {
            // --- SENARYO B: İŞÇİ MODU (DİSKE YAZMA) ---
            // Listede kimse yoksa (targetNode null), demek ki ben bir İşçiyim.
            // Burası değişmedi, sadece yazma işini yapıyor.
            
            String id = request.getMessageId();
            String icerik = request.getContent();

            System.out.println("💾 [Disk] Yazılıyor -> ID: " + id + " | Veri: " + icerik);

            try (FileWriter fw = new FileWriter(fileName, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                out.println(id + ":" + icerik);

                StoreResponse response = StoreResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Kaydedildi (" + fileName + ")")
                        .build();
                responseObserver.onNext(response);

            } catch (IOException e) {
                System.err.println("❌ Disk Hatası: " + e.getMessage());
                responseObserver.onNext(StoreResponse.newBuilder().setSuccess(false).setMessage("Disk hatası").build());
            }
        }
        
        responseObserver.onCompleted();
    }

    /**
     * 3. GET MESSAGE (OKUMA - GET)
     * (Bu kısımda değişiklik yapmadık, aynı kalabilir)
     */
    @Override
    public void getMessage(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        String arananId = request.getMessageId();
        String bulunanIcerik = "";
        boolean bulundu = false;

        System.out.println("🔎 [Disk] Aranıyor -> ID: " + arananId);

        File file = new File(fileName);
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String satir = scanner.nextLine();
                    String[] parcalar = satir.split(":", 2);

                    if (parcalar.length == 2 && parcalar[0].equals(arananId)) {
                        bulundu = true;
                        bulunanIcerik = parcalar[1];
                        break;
                    }
                }
            } catch (FileNotFoundException e) { }
        }

        GetResponse.Builder responseBuilder = GetResponse.newBuilder().setFound(bulundu);

        if (bulundu) {
            System.out.println("✅ [Disk] BULUNDU: " + bulunanIcerik);
            responseBuilder.setContent(bulunanIcerik);
            responseBuilder.setOwnerNode(fileName);
        } else {
            System.out.println("❌ [Disk] Bulunamadı.");
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}