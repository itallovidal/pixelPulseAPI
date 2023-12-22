package api.pixelPulse.pixelPulse.entities;

import api.pixelPulse.pixelPulse.DTOs.DTOUser;
import api.pixelPulse.pixelPulse.PixelPulseApplication;
import api.pixelPulse.pixelPulse.enums.Genres;
import ch.qos.logback.core.encoder.EchoEncoder;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Database {
    // retorna conexão com banco de dados
    private Firestore FirestoreDatabaseConfig() throws IOException {
        ClassLoader classLoader = PixelPulseApplication.class.getClassLoader();
        InputStream serviceAccount = classLoader.getResourceAsStream("serviceAccountKey.json");

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        FirebaseApp app;

        if (FirebaseApp.getApps().isEmpty()) {
            app = FirebaseApp.initializeApp(options, "pixelpulsedb");
        } else {
            app = FirebaseApp.getApps().get(0);
        }

        return  FirestoreClient.getFirestore(app);
    }

    public boolean register(DTOUser user) throws Exception {
        String uuid = UUID.randomUUID().toString();

        try{
            DocumentReference docRef = this.FirestoreDatabaseConfig().collection("users").document(uuid);

            Map<String, String> data = new HashMap<>(){
                {
                    put("email", user.email());
                    put("password", user.password());
                    put("favoriteGenre1", user.favoriteGenre1().toString());
                    put("favoriteGenre2", user.favoriteGenre2().toString());
                    put("gamesLife", user.gamesLife());
                    put("userID", uuid);
                }
            };

            ApiFuture<WriteResult> result = docRef.set(data);
            return true;

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public DTOUser getUser(String email, String password) throws Exception {
        try{
            CollectionReference usersRef = this.FirestoreDatabaseConfig().collection("users");

            Query query = usersRef.whereEqualTo("email", email).whereEqualTo("password", password);

            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            if(querySnapshot.get().getDocuments().isEmpty()){
                throw new Exception("Usuário não encontrado.");
            }

            DocumentSnapshot data = querySnapshot.get().getDocuments().get(0);

            DTOUser user = new DTOUser(
                        data.get("userID").toString(),
                        data.get("email").toString(),
                        data.get("password").toString(),
                        data.get("gamesLife").toString(),
                        Genres.valueOf(data.get("favoriteGenre1").toString()),
                        Genres.valueOf(data.get("favoriteGenre2").toString())
                    );

            return user;

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }
}



