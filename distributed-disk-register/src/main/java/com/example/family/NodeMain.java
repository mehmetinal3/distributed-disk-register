package com.example.family;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;

// Bu dosya projenin beynidir. 
// "Run" tuşuna bastığında Java önce buradaki main metoduna bakar.
public class NodeMain {

    public static void main(String[] args) throws Exception {
        System.out.println("🚀 Sistem başlatılıyor...");

        // 1. ADIM: Komut satırından gelen ayarları oku (Örn: -port 5000)
        // (Bunu Task 1'de yazdığımız CommandParser yapıyor)
        Configuration config = CommandParser.parse(args);

        int myPort = config.getPort();
        System.out.println("✅ Port belirlendi: " + myPort);

        // 2. ADIM: Sunucuyu (Server) Başlat
        // Bu bilgisayarı dışarıdan gelen isteklere açıyoruz.
        Server server = ServerBuilder.forPort(myPort)
                .addService(new FamilyServiceImpl()) // Az önce yazdığımız hizmeti ekle
                .build()
                .start();

        System.out.println("👂 Sunucu " + myPort + " portunda dinlemeye başladı...");

        // Kendi adresimizi kaydedelim (Daha sonra IP bulmayı otomatikleştireceğiz)
        // Şimdilik "localhost" diyoruz.
        String myIp = "localhost"; 

        // 3. ADIM: Eğer bir hedef verildiyse, ona katıl (Client Ol)
        // (Örn: -target 127.0.0.1:5000 denildiyse)
        if (config.getTargetHost() != null) {
            String hedefIp = config.getTargetHost();
            int hedefPort = config.getTargetPort();

            System.out.println("🔗 Hedefe bağlanılıyor: " + hedefIp + ":" + hedefPort);

            // Hedef sunucuya bir kanal (hat) aç
            ManagedChannel channel = ManagedChannelBuilder.forAddress(hedefIp, hedefPort)
                    .usePlaintext() // Güvenlik sertifikası olmadan (geliştirme modu)
                    .build();

            // Karşı tarafla konuşacak "Vekil" (Stub) oluştur
            FamilyServiceGrpc.FamilyServiceBlockingStub stub = FamilyServiceGrpc.newBlockingStub(channel);

            // Kendimizi tanıtan bir kimlik kartı hazırla
            NodeInfo myInfo = NodeInfo.newBuilder()
                    .setHost(myIp)
                    .setPort(myPort)
                    .build();

            try {
                // VE İŞTE O AN: Join metodunu çağır!
                stub.join(myInfo);
                System.out.println("🎉 Başarıyla ağa katıldık!");
            } catch (Exception e) {
                System.err.println("❌ Ağa katılırken hata oluştu: " + e.getMessage());
            }
        } else {
            System.out.println("👑 Hedef belirtilmedi, Lider (ilk düğüm) benim.");
        }

        // 4. ADIM: Sunucuyu açık tut
        // Bu satır olmazsa program hemen kapanır.
        server.awaitTermination();
    }
}