package com.example.family;

import io.grpc.stub.StreamObserver;
import com.example.family.FamilyServiceGrpc.FamilyServiceImplBase;

import java.io.*;
import java.util.Scanner;

/**
 * FamilyServiceImpl
 * GÖREVİ: Dışarıdan gelen "Kaydet (SET)" ve "Getir (GET)" isteklerini yapan sınıftır.
 * ARTIK CHAT YOK, VERİ DEPOLAMA VAR.
 */
public class FamilyServiceImpl extends FamilyServiceImplBase {

    // Her çalışan terminalin (Node) kendi özel dosya ismi olsun.
    // Örn: storage_12345.txt (12345 o anki işlem numarasıdır)
    private final String fileName = "storage_" + ProcessHandle.current().pid() + ".txt";

    /**
     * 1. JOIN (AĞA KATILMA)
     * Yeni gelen üyeyi karşılar.
     */
    @Override
    public void join(NodeInfo request, StreamObserver<JoinResponse> responseObserver) {
        String yeniGelen = request.getHost() + ":" + request.getPort();
        System.out.println("👋 [Lider] Yeni katılım isteği: " + yeniGelen);

        // Liderin hafızasına (Registry) ekle
        NodeRegistry.registerNode(yeniGelen);

        // Cevap dön: "Başarıyla katıldın"
        JoinResponse response = JoinResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Aramıza hoşgeldin! Dosya ismin: " + fileName)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * 2. STORE MESSAGE (KAYDETME - SET)
     * Liderden "Bunu diske yaz" emri geldiğinde çalışır.
     * Hocanın istediği "Üyeler mesajı diskte saklamalıdır" maddesi burasıdır.
     */
    @Override
    public void storeMessage(StoreRequest request, StreamObserver<StoreResponse> responseObserver) {
        String id = request.getMessageId();
        String icerik = request.getContent();

        System.out.println("💾 [Disk] Yazılıyor -> ID: " + id + " | Veri: " + icerik);

        try (FileWriter fw = new FileWriter(fileName, true); // 'true' = dosyanın sonuna ekle
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            // Dosyaya şu formatta yazıyoruz: ID:İÇERİK
            out.println(id + ":" + icerik);

            // Başarılı cevabı dön
            StoreResponse response = StoreResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Kaydedildi: " + fileName)
                    .build();
            responseObserver.onNext(response);

        } catch (IOException e) {
            System.err.println("❌ Disk Hatası: " + e.getMessage());
            // Hata cevabı dön
            StoreResponse response = StoreResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Disk hatası oluştu!")
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    /**
     * 3. GET MESSAGE (OKUMA - GET)
     * Lider "Şu ID'li mesaj sende mi?" diye sorduğunda çalışır.
     * Dosyayı satır satır okur ve aranan ID'yi bulmaya çalışır.
     */
    @Override
    public void getMessage(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        String arananId = request.getMessageId();
        String bulunanIcerik = "";
        boolean bulundu = false;

        System.out.println("🔎 [Disk] Aranıyor -> ID: " + arananId);

        // Dosyayı okumaya çalış
        File file = new File(fileName);
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String satir = scanner.nextLine();
                    // Satır formatımız: ID:İÇERİK (Örn: 100:Merhaba)
                    String[] parcalar = satir.split(":", 2);

                    if (parcalar.length == 2) {
                        String dosyadakiId = parcalar[0];
                        String dosyadakiIcerik = parcalar[1];

                        if (dosyadakiId.equals(arananId)) {
                            bulundu = true;
                            bulunanIcerik = dosyadakiIcerik;
                            break; // Bulduk, döngüden çık
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                // Dosya yoksa sorun değil, bulunamadı deriz.
            }
        }

        // Sonucu hazırla
        GetResponse.Builder responseBuilder = GetResponse.newBuilder()
                .setFound(bulundu);

        if (bulundu) {
            System.out.println("✅ [Disk] BULUNDU: " + bulunanIcerik);
            responseBuilder.setContent(bulunanIcerik);
            responseBuilder.setOwnerNode(fileName); // Kimde bulunduğunu da söyleyelim
        } else {
            System.out.println("❌ [Disk] Bulunamadı.");
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}