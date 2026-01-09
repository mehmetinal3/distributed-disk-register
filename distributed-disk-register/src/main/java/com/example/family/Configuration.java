package com.example.family;  // Kodumuzu düzenli olsun diye family paketinin altına koyduk.

import java.io.File;  // Dosya okuma işlemleri için Java'nın standart kütüphanelerini çağırdık.
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Configuration {
    
    private static final String CONFIG_FILE = "tolerance.conf"; // Dosya ismini kodun her yerinde tekrar tekrar yazmamak için en tepeye sabit olarak tanımladık. İsmi değişirse sadece buradan değiştireceğiz.

    // Bu fonksiyon dosyayı okuyup içindeki sayıyı bize verecek
    public static int getToleranceLevel() {
        try {
            // Dosya proje ana dizininde olduğu için direkt ismini veriyoruz
            File file = new File(CONFIG_FILE);  // Dosyayı hafızada bir nesne olarak oluşturuyoruz.
            Scanner scanner = new Scanner(file);  // Dosyanın içini okuyacak tarayıcıyı başlatıyoruz.
            
            if (scanner.hasNextInt()) { // Güvenlik kontrolü. Dosyanın içinde gerçekten bir tamsayı var mı diye bakıyoruz. Eğer harf varsa hata vermesin diye kontrol ediyoruz.
                int tolerance = scanner.nextInt();
                System.out.println("Konfigürasyon okundu. Tolerans Seviyesi: " + tolerance);
                scanner.close();
                return tolerance; // Okuduğumuz sayıyı sisteme geri döndürüyoruz.
            }
            scanner.close();
        /* 
          Hocam, try-catch kullanmamın sebebi Exception Handling (Hata Yönetimi). 
          Eğer tolerance.conf dosyası yanlışlıkla silinirse veya bulunamazsa sistem çökmesin istedim. 
          catch bloğunda hatayı yakalayıp, sisteme varsayılan (default) olarak 1 değerini döndürüyorum. 
          Böylece dosya olmasa bile program çalışmaya devam eder.    
        */
        } catch (FileNotFoundException e) {
            System.out.println("UYARI: tolerance.conf dosyası bulunamadı! Varsayılan değer (1) kullanılıyor.");
        }
        
        return 1; // Dosya yoksa veya hata varsa varsayılan olarak 1 döner
    }
    
    // Test etmek için main metodu
    public static void main(String[] args) {
        getToleranceLevel();
    }
}