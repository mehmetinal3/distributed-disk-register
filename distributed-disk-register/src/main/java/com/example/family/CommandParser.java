package com.example.family;

/*
 İstemciden gelen ham string verileri (örn: "SET 100 Merhaba") 
 sistemin anlayacağı parçalara ayıran (Parser) sınıf.
*/
public class CommandParser {

    // Komut tiplerini burada tanımlıyoruz (Enum)
    // NOT: String yerine Enum kullanmamın sebebi "Tip Güvenliği" sağlamak.
    // Hatalı yazımların (örn: "set" yerine "sEt") önüne geçer.
    public enum CommandType {
        SET,     // Veri kaydetme komutu
        GET,     // Veri okuma komutu
        UNKNOWN  // Hatalı veya bilinmeyen komut
    }

    // Ayrıştırılmış (Parse edilmiş) komutu tutacak paketimiz (Static Nested Class)
    // Bu sınıf, ayrıştırma sonucu elde edilen Veri Tipi, Anahtar ve Değer'i bir arada tutar.
    public static class ParsedCommand {
        public CommandType type;
        public String key;      // message_id (Örn: 100)
        public String value;    // message (Örn: Merhaba)

        public ParsedCommand(CommandType type, String key, String value) {
            this.type = type;
            this.key = key;
            this.value = value;
        }
    }

    // ANA METOT: Gelen yazıyı (String) alıp parçalara ayıran fonksiyon
    public static ParsedCommand parse(String input) {
        // 1. Defansif Programlama (Defensive Programming):
        // Gelen veri null ise veya boşsa, sistemin çökmesini (NullPointerException) engellemek için kontrol ediyoruz.
        if (input == null || input.trim().isEmpty()) {
            return new ParsedCommand(CommandType.UNKNOWN, null, null);
        }

        // 2. Mesajı boşluklara göre bölüyoruz (En fazla 3 parça: KOMUT, ID, MESAJ)
        // Regex Kullanımı ("\\s+"): Birden fazla boşluk olsa bile tek boşluk gibi algılar.
        // Limit (3): Mesajın (value) içindeki boşlukları bölmemek için en fazla 3 parçaya ayırırız.
        // Örn: "SET 100 Merhaba Dunya" -> ["SET", "100", "Merhaba Dunya"]
        String[] parts = input.trim().split("\\s+", 3); 
        
        // Kullanıcı küçük harfle "set" yazsa bile biz onu "SET" olarak algılamak için büyütüyoruz.
        String commandStr = parts[0].toUpperCase(); 

        // 3. SET Komutu Kontrolü
        if ("SET".equals(commandStr)) {
            // Validasyon: SET komutunda en az 3 parça olmalı: SET + ID + MESAJ
            if (parts.length < 3) {
                return new ParsedCommand(CommandType.UNKNOWN, null, null);
            }
            return new ParsedCommand(CommandType.SET, parts[1], parts[2]);
        } 
        // 4. GET Komutu Kontrolü
        else if ("GET".equals(commandStr)) {
            // Validasyon: GET komutunda en az 2 parça olmalı: GET + ID
            if (parts.length < 2) {
                return new ParsedCommand(CommandType.UNKNOWN, null, null);
            }
            return new ParsedCommand(CommandType.GET, parts[1], null);
        }

        // Eğer komut SET veya GET değilse, sistem bunu "Bilinmeyen Komut" olarak işaretler.
        return new ParsedCommand(CommandType.UNKNOWN, null, null);
    }

    // --- TEST KISMI (Kodun çalışıp çalışmadığını denemek için Unit Test benzeri yapı) ---
    public static void main(String[] args) {
        System.out.println("--- Parser Testi Başlıyor ---");

        // Test 1: Doğru bir SET komutu deneyelim
        String test1 = "SET 100 Sistem Programlama Odevi";
        ParsedCommand sonuc1 = parse(test1);
        System.out.println("Girdi: " + test1);
        System.out.println("Algılanan Tip: " + sonuc1.type);
        System.out.println("ID: " + sonuc1.key);
        System.out.println("Mesaj: " + sonuc1.value);
        System.out.println("-----------------------------");

        // Test 2: Doğru bir GET komutu deneyelim
        String test2 = "GET 500";
        ParsedCommand sonuc2 = parse(test2);
        System.out.println("Girdi: " + test2);
        System.out.println("Algılanan Tip: " + sonuc2.type);
        System.out.println("ID: " + sonuc2.key);
    }
}