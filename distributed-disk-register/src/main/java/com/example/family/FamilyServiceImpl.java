package com.example.family;

import io.grpc.stub.StreamObserver;
import com.example.family.FamilyServiceGrpc.FamilyServiceImplBase;
// Yeni eklenen kütüphaneler (Yönlendirme yapmak için)
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.*;
import java.util.Scanner;

/**
 * FamilyServiceImpl
 * GÖREVİ: Dışarıdan gelen "Kaydet (SET)" ve "Getir (GET)" isteklerini yapan sınıftır.
 * ARTIK CHAT YOK, VERİ DEPOLAMA VE YÜK DENGELEME VAR.
 */
public class FamilyServiceImpl extends FamilyServiceImplBase {

    // Her çalışan terminalin (Node) kendi özel dosya ismi olsun.
    // Örn: storage_12345.txt
    private final String fileName = "storage_" + ProcessHandle.current().pid() + ".txt";

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
     * 2. STORE MESSAGE (KAYDETME - LOAD BALANCING)
     * BURASI DEĞİŞTİ: Artık hem kayıt yapıyor hem de yönlendirme!
     */
    @Override
    public void storeMessage(StoreRequest request, StreamObserver<StoreResponse> responseObserver) {
        // Önce Registry'e bak: Yönlendirecek kimse var mı?
        String targetNode = NodeRegistry.getNextNode();

        if (targetNode != null) {
            // --- SENARYO A: LİDER MODU (YÖNLENDİRME) ---
            // Listede eleman varsa, ben Liderim demektir. İş bende kalmaz, dağıtırım.
            
            System.out.println("🔀 [LoadBalancer] Gelen yükü şuna yönlendiriyorum: " + targetNode);

            // Hedefin adresini parçala (localhost:6001 -> host, port)
            String[] parts = targetNode.split(":");
            String host = parts[0];
            int port = Integer.parseInt(parts[1]);

            // Hedefe bağlan (Anlık bir istemci oluşturuyoruz)
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext()
                    .build();
            
            try {
                // Hedefin storeMessage metodunu uzaktan çağır
                FamilyServiceGrpc.FamilyServiceBlockingStub stub = FamilyServiceGrpc.newBlockingStub(channel);
                StoreResponse responseFromWorker = stub.storeMessage(request);
                
                // İşçiden gelen cevabı, asıl istemciye ilet
                responseObserver.onNext(responseFromWorker);
                
            } catch (Exception e) {
                System.err.println("❌ Yönlendirme Hatası: " + e.getMessage());
                responseObserver.onNext(StoreResponse.newBuilder().setSuccess(false).setMessage("Yönlendirme hatası").build());
            } finally {
                channel.shutdown(); // İş bitince kanalı kapat
            }

        } else {
            // --- SENARYO B: İŞÇİ MODU (DİSKE YAZMA) ---
            // Listede kimse yoksa (targetNode null), demek ki ben bir İşçiyim (veya yalnızım).
            // Emri aldım, diske yazıyorum.
            
            String id = request.getMessageId();
            String icerik = request.getContent();

            System.out.println("💾 [Disk] Yazılıyor -> ID: " + id + " | Veri: " + icerik);

            try (FileWriter fw = new FileWriter(fileName, true); // 'true' = ekleme modu
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                // Dosyaya yaz: ID:İÇERİK
                out.println(id + ":" + icerik);

                StoreResponse response = StoreResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Kaydedildi (" + fileName + ")") // Kimin kaydettiği görünsün
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