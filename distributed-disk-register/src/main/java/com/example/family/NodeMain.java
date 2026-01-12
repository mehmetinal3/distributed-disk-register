package com.example.family;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.util.Scanner; // Klavye okumak için ekledik

// Bu dosya projenin beynidir. 
// "Run" tuşuna bastığında Java önce buradaki main metoduna bakar.
public class NodeMain {

    public static void main(String[] args) throws Exception {
        System.out.println("🚀 Sistem başlatılıyor...");

        // 1. ADIM: Komut satırından gelen ayarları oku (Örn: -port 5000)
        Configuration config = CommandParser.parse(args);

        int myPort = config.getPort();
        System.out.println("✅ Port belirlendi: " + myPort);

        // 2. ADIM: Sunucuyu (Server) Başlat
        // Bu bilgisayarı dışarıdan gelen isteklere açıyoruz.
        Server server = ServerBuilder.forPort(myPort)
                .addService(new FamilyServiceImpl()) 
                .build()
                .start();

        System.out.println("👂 Sunucu " + myPort + " portunda dinlemeye başladı...");

        // Kendi adresimizi belirleyelim
        String myIp = "localhost"; 
        
        // İletişim kuracağımız "Vekil" (Stub) nesnesi.
        // Bunu if bloğunun dışına çıkardık ki aşağıda mesaj atarken de kullanabilelim.
        FamilyServiceGrpc.FamilyServiceBlockingStub targetStub = null;

        // 3. ADIM: Eğer bir hedef verildiyse, ona katıl (Client Ol)
        if (config.getTargetHost() != null) {
            String hedefIp = config.getTargetHost();
            int hedefPort = config.getTargetPort();

            System.out.println("🔗 Hedefe bağlanılıyor: " + hedefIp + ":" + hedefPort);

            ManagedChannel channel = ManagedChannelBuilder.forAddress(hedefIp, hedefPort)
                    .usePlaintext()
                    .build();

            // Stub'ı oluşturuyoruz
            targetStub = FamilyServiceGrpc.newBlockingStub(channel);

            // Kendimizi tanıtan bir kimlik kartı hazırla
            NodeInfo myInfo = NodeInfo.newBuilder()
                    .setHost(myIp)
                    .setPort(myPort)
                    .build();

            try {
                // Join metodunu çağır!
                targetStub.join(myInfo);
                System.out.println("🎉 Başarıyla ağa katıldık!");
            } catch (Exception e) {
                System.err.println("❌ Ağa katılırken hata oluştu: " + e.getMessage());
            }
        } else {
            System.out.println("👑 Hedef belirtilmedi, Lider (ilk düğüm) benim.");
        }

        // --- 4. ADIM: MESAJLAŞMA DÖNGÜSÜ ---
        // Eskiden burada sadece bekliyorduk, şimdi hem bekliyoruz hem klavyeyi dinliyoruz.
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("💬 Sohbet başladı! Mesajını yaz ve Enter'a bas:");

        while (true) {
            // Kullanıcının yazmasını bekle
            String messageText = scanner.nextLine();

            // Boş enter'a basarsa işlem yapma
            if (messageText.trim().isEmpty()) continue;

            // Eğer bir hedefe bağlıysak (Lider değilsek) mesajı gönderelim
            if (targetStub != null) {
                try {
                    // Proto dosyasındaki ChatMessage yapısını dolduruyoruz.
                    // NOT: Senin proto dosyanda "message" yerine "text", "from" yerine "fromHost/fromPort" var.
                    ChatMessage chatMsg = ChatMessage.newBuilder()
                            .setFromHost(myIp)        // Kimden (IP)
                            .setFromPort(myPort)      // Kimden (Port)
                            .setText(messageText)     // Mesaj İçeriği (setMessage DEĞİL, setText)
                            .setTimestamp(System.currentTimeMillis()) // Zaman damgası
                            .build();

                    // gRPC ile karşıya fırlat!
                    targetStub.receiveChat(chatMsg);
                    System.out.println("📤 Gönderildi: " + messageText);
                    
                } catch (Exception e) {
                    System.err.println("❌ Mesaj giderken hata oldu: " + e.getMessage());
                }
            } else {
                // Eğer Lidersek ve bir yere bağlı değilsek, kendi kendimize konuşuyoruz demektir.
                System.out.println("👑 [Lider Notu]: Ben başkomutanım, şu an mesajı sadece kendime yazdım: " + messageText);
            }
        }
    }
}