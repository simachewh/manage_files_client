package managefilesclient.sime.seven;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import managefilesclient.sime.seven.model.FileModel;
import managefilesclient.sime.seven.model.FilesListModel;

import com.fasterxml.jackson.jaxrs.json.annotation.JacksonFeatures;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

public class ClientHandler {
    
    private final String BASE_URI = "http://localhost:8080/managefiles/webapi/";
    private final String FILE_LIST_URI = BASE_URI + "files/";
    private final String UPLOAD_URI = BASE_URI + "files/upload";
    private final String DOWNLOAD_URI = BASE_URI + "files/download/";
    private final Client client;

    @SuppressWarnings("deprecation")
    public ClientHandler() {
        client = ClientBuilder.newClient();
        client.register(JacksonFeatures.class);
    }

    /**
     * A test method.
     *
     * @return A string if it was able to communicate with the service.
     */
    @SuppressWarnings("deprecation")
    public String test() {
        String str;
        str = client
                .target(FILE_LIST_URI)
                .request().get(String.class);
        System.out.println(str);
        return str;
    }

    /**
     * Sends request to the web API to get the list of files.
     *
     * @return An array list of the file names as Strings.
     */
    public ArrayList<String> getListOfFiles() {
        ArrayList<String> list = new ArrayList<>();
        FilesListModel listResponse;
        List<FileModel> model = null;
       
        try {
             listResponse = client.
                    target(FILE_LIST_URI)
                    .request().get(FilesListModel.class);
        } catch (ProcessingException pex) {
            listResponse = null;
            return list;
        }

        model = listResponse.getFilesList();
        model.stream().forEach((fileModel) -> {
            list.add(fileModel.getFileName());
        });

        return list;
    }

    /**
     * Client method to upload file to the web API file upload resource.
     *
     * @param file The file to upload.
     */
    public void upload(File file) {
        client.register(MultiPartFeature.class);
        WebTarget webTarget = client.target(UPLOAD_URI);
        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
        FileDataBodyPart fileBodyPart = new FileDataBodyPart("file", file,
                MediaType.MULTIPART_FORM_DATA_TYPE);
        multiPart.bodyPart(fileBodyPart);
        Response response = webTarget.request(MediaType.APPLICATION_JSON).post(
                Entity.entity(multiPart, multiPart.getMediaType()));
        int code = response.getStatusInfo().getStatusCode();
        if (Status.OK.getStatusCode() == code) {

        } else {

        }
    }

    public void download(String fileName, File toSaveOn) {
        Response response = client.target(DOWNLOAD_URI + fileName).request().get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println("response was ok");
            InputStream is = response.readEntity(InputStream.class);
            saveFile(is, toSaveOn);
        }
    }

    /**
     * Gets a stream of the file by the given name from the server.
     *
     * @param fileName The name of the file to preview.
     * @return The {@link InputStream} that streams the contents of the file by
     * the given name. Null if the stram was not available from the service.
     */
    public InputStream preview(String fileName) {
        Response response = client.target(DOWNLOAD_URI + fileName).request().get();
        InputStream is = null;
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            is = response.readEntity(InputStream.class);
        }
        return is;
    }

    /**
     * Writes the given input stream to the given file.
     *
     * @param is the {@link InputStream } that has content.
     * @param saveOn A {@link File} to write the stream on.
     */
    private void saveFile(InputStream is, File saveOn) {

        try {
            OutputStream os = new FileOutputStream(saveOn);
            int read = 0;
            byte[] bytes = new byte[8 * 1024];

            while ((read = is.read(bytes)) != -1) {
                os.write(bytes, 0, read);
                bytes = new byte[1024];
            }
            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
//    public static void main(String[] args) {
//        ClientHandler ch = new ClientHandler();
//        ArrayList<String> list = ch.getListOfFiles();
//        list.stream().forEach((str) -> {
//            System.out.println(str);
//        });
//    }
}
