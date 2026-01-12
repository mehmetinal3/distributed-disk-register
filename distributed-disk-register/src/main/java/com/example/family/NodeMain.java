package com.example.family;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.util.Scanner;

// Bu dosya hem SUNUCU (Server) hem de İSTEMCİ (Client) gibi davranır.
// Hem gelen istekleri dinler, hem de komut gönderir.
public class NodeMain {

    public static void main(String[] args) throws Exception {
        System.out.println("🚀 Depolama Sistemi (Storage Node) Başlatılıyor...");

        // 1. Ayarları Oku (-port 6000 -target localhost:5000 vb.)
        Configuration config = CommandParser.parse(args);
        int myPort = config.getPort();

        // 2. Sunucuyu Başlat (Gelen SET/GET isteklerini dinle)
        Server server = ServerBuilder.forPort(myPort)
                .addService(new FamilyServiceImpl()) // Yeni yazdığımız disk servisi
                .build()
                .start();

        System.out.println("👂 Sunucu " + myPort + " portunda dinlemeye başladı...");
        System.out.println("💾 Veriler 'storage_" + ProcessHandle.current().pid() + ".txt' dosyasına yazılacak.");

        // Lider veya Başka Üye ile İletişim Kurmak İçin Kanal
        FamilyServiceGrpc.FamilyServiceBlockingStub targetStub = null;
        String myIp = "localhost";

        // 3. Eğer bir hedef verildiyse ona bağlan (Join at)
        if (config.getTargetHost() != null) {
            String hedefIp = config.getTargetHost();
            int hedefPort = config.getTargetPort();
            System.out.println("🔗 Hedefe bağlanılıyor: " + hedefIp + ":" + hedefPort);

            ManagedChannel channel = ManagedChannelBuilder.forAddress(hedefIp, hedefPort)
                    .usePlaintext()
                    .build();

            targetStub = FamilyServiceGrpc.newBlockingStub(channel);

            // Kendimizi Tanıtalım
            NodeInfo myInfo = NodeInfo.newBuilder().setHost(myIp).setPort(myPort).build();
            try {
                JoinResponse response = targetStub.join(myInfo);
                if (response.getSuccess()) {
                    System.out.println("✅ " + response.getMessage());
                }
            } catch (Exception e) {
                System.err.println("❌ Bağlantı hatası: " + e.getMessage());
            }
        } else {
            System.out.println("👑 Hedef yok, Lider (Baş Düğüm) benim.");
        }

        // --- 4. KOMUT DÖNGÜSÜ (SET ve GET) ---
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n💡 KOMUTLAR:");
        System.out.println("   👉 SET <id> <veri>  (Örn: SET 100 VizeNotlari)");
        System.out.println("   👉 GET <id>         (Örn: GET 100)");
        System.out.println("--------------------------------------------------");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) continue;

            // Komutu parçala: "SET 100 Veri" -> ["SET", "100", "Veri"]
            String[] parts = line.split(" ", 3);
            String command = parts[0].toUpperCase();

            // Eğer bir hedefe bağlı değilsek komut gönderemeyiz (Lidersek kendimize mi yazacağız? Şimdilik hayır)
            if (targetStub == null) {
                System.out.println("⚠️ Lider modundasın. Komutları 'Client' modundaki terminallerden gönder.");
                continue;
            }

            try {
                // --- SET KOMUTU (VERİ KAYDETME) ---
                if (command.equals("SET") && parts.length == 3) {
                    String id = parts[1];
                    String content = parts[2];

                    StoreRequest request = StoreRequest.newBuilder()
                            .setMessageId(id)
                            .setContent(content)
                            .build();

                    StoreResponse response = targetStub.storeMessage(request);
                    System.out.println(response.getSuccess() ? "✅ " + response.getMessage() : "❌ Hata: " + response.getMessage());

                // --- GET KOMUTU (VERİ OKUMA) ---
                } else if (command.equals("GET") && parts.length == 2) {
                    String id = parts[1];

                    GetRequest request = GetRequest.newBuilder()
                            .setMessageId(id)
                            .build();

                    GetResponse response = targetStub.getMessage(request);
                    if (response.getFound()) {
                        System.out.println("📦 BULUNDU (" + response.getOwnerNode() + "): " + response.getContent());
                    } else {
                        System.out.println("🚫 Bulunamadı.");
                    }

                } else {
                    System.out.println("❓ Hatalı komut! Örnek: 'SET 50 Elma' veya 'GET 5'");
                }

            } catch (Exception e) {
                System.err.println("🔥 İletişim Hatası: " + e.getMessage());
            }
        }
    }
}