package com.example.family;

import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * NodeRegistry: Dağıtık sistemdeki tüm aktif düğümlerin (Nodes) listesini tutan sınıf.
 * Sistemin "Kimler burada?" sorusuna cevap verir.
 */
public class NodeRegistry {

    // NEDEN ConcurrentHashMap KULLANDIK?
    // Çünkü dağıtık sistemlerde aynı anda birden fazla düğüm "Ben geldim" diyebilir.
    // ConcurrentHashMap "Thread-Safe" (İplik güvenli) olduğu için veri kaybını önler.
    // Key: Düğüm Adresi (örn: "localhost:5001"), Value: Son görülme zamanı (Heartbeat)
    private static final ConcurrentHashMap<String, Long> nodes = new ConcurrentHashMap<>();

    /**
     * Sisteme yeni bir düğüm ekler veya var olanın süresini günceller.
     * @param address Düğümün adresi (Örn: localhost:5001)
     */
    public static void registerNode(String address) {
        // System.currentTimeMillis(): Düğümün en son ne zaman canlı olduğunu tutar.
        nodes.put(address, System.currentTimeMillis());
        System.out.println("[Registry] Düğüm listeye eklendi/güncellendi: " + address);
    }

    /**
     * Sistemden ayrılan veya çöken düğümü listeden siler.
     * @param address Silinecek düğüm adresi
     */
    public static void removeNode(String address) {
        if (nodes.containsKey(address)) {
            nodes.remove(address);
            System.out.println("[Registry] Düğüm listeden silindi: " + address);
        } else {
            System.out.println("[Registry] HATA: Silinecek düğüm bulunamadı -> " + address);
        }
    }

    /**
     * Şu an aktif olan tüm düğümlerin listesini verir.
     * @return Düğüm adreslerinin listesi
     */
    public static List<String> getActiveNodes() {
        return new ArrayList<>(nodes.keySet());
    }

    // --- TEST METODU (Main) ---
    // Bu sınıfın tek başına doğru çalışıp çalışmadığını test ediyoruz.
    public static void main(String[] args) {
        System.out.println("--- NodeRegistry Test Başlıyor ---");

        // 1. İki tane hayali düğüm ekleyelim
        registerNode("localhost:5001");
        registerNode("localhost:5002");

        // 2. Listeyi kontrol edelim
        System.out.println("Şu anki Düğümler: " + getActiveNodes());

        // 3. Birini silelim
        removeNode("localhost:5001");

        // 4. Son durumu görelim
        System.out.println("Silme Sonrası Düğümler: " + getActiveNodes());
        
        System.out.println("--- Test Bitti ---");
    }
}