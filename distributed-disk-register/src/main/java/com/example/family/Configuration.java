package com.example.family;

/**
 * Configuration Sınıfı
 * Amaç: Uygulama başlatılırken belirlenen ayarları (Port numarası, Hedef Sunucu vb.)
 * hafızada tutmak ve diğer sınıflara (özellikle NodeMain'e) sunmaktır.
 */
public class Configuration {
    
    // Değişkenleri 'private' yaparak Kapsülleme (Encapsulation) ilkesine uyuyoruz.
    // Yani bu verilere dışarıdan kafamıza göre erişip değiştiremeyiz, sadece izin verilen metotlarla okuyabiliriz.
    private int port;           // Bizim sunucumuzun çalışacağı kapı numarası (Örn: 5000)
    private String targetHost;  // Bağlanmak istediğimiz hedef sunucunun IP adresi (Örn: localhost)
    private int targetPort;     // Hedef sunucunun port numarası (Örn: 5001)

    // Kurucu Metot (Constructor)
    // Sınıf oluşturulurken bu değerlerin verilmesini zorunlu kılıyoruz.
    public Configuration(int port, String targetHost, int targetPort) {
        this.port = port;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
    }

    // --- GETTER METOTLARI ---
    // NodeMain sınıfı ayarları okumak istediğinde bu metotları kullanacak.
    
    public int getPort() {
        return port;
    }

    public String getTargetHost() {
        // Eğer hedef belirtilmemişse (null ise), bağlanılacak bir yer yok demektir.
        return targetHost;
    }

    public int getTargetPort() {
        return targetPort;
    }
}