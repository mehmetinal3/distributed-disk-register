package com.example.family;

import java.util.ArrayList;
import java.util.List;

/**
 * NodeRegistry: Dağıtık sistemdeki tüm aktif düğümlerin (Nodes) listesini tutan sınıf.
 * Sistemin "Kimler burada?" sorusuna cevap verir.
 * GÜNCELLEME: Artık Yük Dengeleme (Load Balancing) için sıra takibi de yapıyor.
 */
public class NodeRegistry {

    // --- DEĞİŞİKLİK ---
    // Round Robin (Sırayla Dağıtım) yapabilmek için sıraya (Index) ihtiyacımız var.
    // Bu yüzden Map yerine List kullanıyoruz.
    // Yine de "Thread-Safe" olması için metotlara 'synchronized' ekledik.
    private static final List<String> nodes = new ArrayList<>();
    
    // Sıranın kimde olduğunu tutan sayaç (0, 1, 2...)
    private static int nextNodeIndex = 0;

    /**
     * Sisteme yeni bir düğüm ekler.
     * synchronized: Aynı anda iki düğüm eklenirse liste karışmasın diye kilitler.
     */
    public static synchronized void registerNode(String address) {
        if (!nodes.contains(address)) {
            nodes.add(address);
            System.out.println("[Registry] Yeni üye eklendi: " + address);
            System.out.println("📊 Güncel Üye Sayısı: " + nodes.size());
        }
    }

    /**
     * Sistemden ayrılan düğümü siler.
     */
    public static synchronized void removeNode(String address) {
        if (nodes.remove(address)) {
            System.out.println("[Registry] Düğüm silindi: " + address);
            // Liste boyutu değiştiği için index hatası olmasın diye sıfırlayalım
            nextNodeIndex = 0; 
        } else {
            System.out.println("[Registry] HATA: Silinecek düğüm bulunamadı -> " + address);
        }
    }

    /**
     * --- YENİ METOT: ROUND ROBIN MANTIĞI ---
     * Lider, gelen işi kime vereceğini buradan öğrenir.
     * Sırasıyla her çağrışta bir sonraki üyeyi verir.
     */
    public static synchronized String getNextNode() {
        if (nodes.isEmpty()) {
            return null; // Kimse yoksa null dön (İşi kendin yap)
        }
        
        // Listeden sıradaki kişiyi al
        String target = nodes.get(nextNodeIndex);
        
        // Sayacı bir artır. Listenin sonuna geldiysek başa (0) dön.
        // Modülo (%) işlemi burada döngüyü sağlar (Örn: 3 % 3 = 0).
        nextNodeIndex = (nextNodeIndex + 1) % nodes.size();
        
        return target;
    }

    /**
     * Aktif düğümleri listeler.
     */
    public static synchronized List<String> getActiveNodes() {
        return new ArrayList<>(nodes);
    }
}