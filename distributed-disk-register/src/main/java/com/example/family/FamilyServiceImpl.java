package com.example.family;

// gRPC'nin iletişim kurması için gereken kütüphaneler
import io.grpc.stub.StreamObserver;
// Proto dosyasından üretilen temel sınıf (Base Class)
import com.example.family.FamilyServiceGrpc.FamilyServiceImplBase;

/**
 * BU SINIF NE İŞE YARAR?
 * Burası bizim "Çağrı Merkezimiz".
 * Dış dünyadan (başka bilgisayarlardan) gelen tüm istekleri karşılayan yerdir.
 * .proto dosyasında tanımladığımız 3 kuralı (Join, ReceiveChat, GetFamily) burada kodluyoruz.
 */
public class FamilyServiceImpl extends FamilyServiceImplBase {

    /**
     * 1. GÖREV: JOIN (Ağa Katılma)
     * Yeni bir bilgisayar ağa katılmak istediğinde bu metodu çağırır.
     * @param request          : Gelen kişinin bilgileri (IP adresi ve Portu)
     * @param responseObserver : Cevabı geri göndereceğimiz "postacı"
     */
    @Override
    public void join(NodeInfo request, StreamObserver<FamilyView> responseObserver) {
        // Gelen kişinin IP ve Port bilgilerini alıyoruz
        String yeniGelenHost = request.getHost();
        int yeniGelenPort = request.getPort();
        String tamAdres = yeniGelenHost + ":" + yeniGelenPort;

        // Konsola bilgi verelim
        System.out.println("👋 [Sunucu] Yeni katılım isteği geldi: " + tamAdres);

        // ÖNEMLİ: Gelen kişiyi "Rehberimize" (NodeRegistry) kaydediyoruz.
        // Böylece sistemde kimler var unutmayacağız.
        NodeRegistry.registerNode(tamAdres);

        // Cevap Hazırlama:
        // Senin proto dosyan Join işleminden sonra "FamilyView" dönmemizi istiyor.
        FamilyView response = FamilyView.newBuilder().build();

        // Cevabı postacıya verip gönderiyoruz
        responseObserver.onNext(response);
        
        // "İşimiz bitti, telefonu kapatabilirsin" diyoruz.
        responseObserver.onCompleted();
    }

    /**
     * 2. GÖREV: RECEIVE CHAT (Mesaj Alma)
     * Biri bize mesaj attığında bu metot çalışır.
     */
    @Override
    public void receiveChat(ChatMessage request, StreamObserver<Empty> responseObserver) {
        // Gelen mesajın kimden geldiğini ve içeriğini alalım.
        // Proto dosyasındaki 'fromHost', 'fromPort' ve 'text' alanlarını kullanıyoruz.
        String kimden = request.getFromHost() + ":" + request.getFromPort();
        String mesaj = request.getText(); // getMessage() DEĞİL, getText() kullanıyoruz.

        // Mesajı ekrana şık bir şekilde basalım
        System.out.println("\n💬 [CHAT] " + kimden + " diyor ki: " + mesaj);
        
        // Karşı tarafa "Mesajını aldım" demek için boş bir cevap (Empty) dönüyoruz.
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
    
    /**
     * 3. GÖREV: GET FAMILY (Üye Listesini İsteme)
     * Biri "Sistemde kimler var?" diye sorarsa burası çalışır.
     */
    @Override
    public void getFamily(Empty request, StreamObserver<FamilyView> responseObserver) {
        // Şimdilik sadece boş bir liste dönüyoruz.
        // Amaç: Kodun hata vermeden çalışması.
        FamilyView response = FamilyView.newBuilder().build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}