package com.example.family;

/**
 * CommandParser Sınıfı
 * Amaç: Terminalden girilen karmaşık komut satırı argümanlarını (String[] args)
 * programın anlayacağı temiz bir 'Configuration' nesnesine dönüştürmektir.
 */
public class CommandParser {

    // Main metodundan gelen 'args' dizisini alıp işleyen ana fonksiyon
    public static Configuration parse(String[] args) {
        
        // 1. Varsayılan Ayarlar (Default Values)
        // Eğer kullanıcı hiçbir şey yazmazsa programın çökmemesi için varsayılan değerler atıyoruz.
        int port = 5000;          // Varsayılan port
        String targetHost = null; // Varsayılan hedef (Yok)
        int targetPort = 0;       // Varsayılan hedef portu

        // 2. Argümanları Tarama (Parsing Loop)
        // Kullanıcının girdiği her kelimeyi tek tek kontrol ediyoruz.
        for (int i = 0; i < args.length; i++) {
            
            // SENARYO A: Kullanıcı kendi portunu belirlemek istiyor (-port 5000)
            if ("-port".equals(args[i]) && i + 1 < args.length) {
                try {
                    // String olarak gelen "5000" yazısını sayıya (int) çeviriyoruz.
                    port = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    System.err.println("Hata: Port numarası sayı olmalıdır! Varsayılan (5000) kullanılıyor.");
                }
            } 
            
            // SENARYO B: Kullanıcı bir hedefe bağlanmak istiyor (-target 127.0.0.1:6000)
            else if ("-target".equals(args[i]) && i + 1 < args.length) {
                String targetFull = args[i + 1]; // Örn: "127.0.0.1:6000"
                
                // IP ve Portu birbirinden ayırmak için ":" işaretinden bölüyoruz.
                String[] parts = targetFull.split(":");
                
                if (parts.length == 2) {
                    targetHost = parts[0]; // "127.0.0.1" kısmını al
                    try {
                        targetPort = Integer.parseInt(parts[1]); // "6000" kısmını sayıya çevirip al
                    } catch (NumberFormatException e) {
                        System.err.println("Hata: Hedef port geçersiz.");
                    }
                } else {
                    System.out.println("Uyarı: Hedef formatı yanlış. Örnek: -target ip:port");
                }
            }
        }

        // 3. Sonuç Döndürme
        // Topladığımız veya varsayılan olarak kalan tüm bilgileri paketleyip geri gönderiyoruz.
        return new Configuration(port, targetHost, targetPort);
    }
}