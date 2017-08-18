package br.com.campuscode03.contactapp.network;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.campuscode03.contactapp.model.Contact;


public class ContactsHttpManager {

    private static final String CONTACTS_URL = "http://contatostreinamento.herokuapp.com/contacts";

    public interface ReloadContacts {
        void onContactsSync(List <Contact> contacts);
    }

    public class SendContacts extends AsyncTask<String, Void, String> {

        String name;
        String phone;

        public SendContacts(String name, String phone){
            this.name = name;
            this.phone = phone;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                postContact(name, phone);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public class ReceiveContacts extends AsyncTask<String, Void, String> {

        ReloadContacts reloadListener;
        List <Contact> listContacts;

        public ReceiveContacts(ReloadContacts reloadListener){
            this.reloadListener = reloadListener;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result;
            try {
                result = getContacts();
                listContacts = parseContacts(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected List<Contact> parseContacts(String result){
            List<Contact> listContacts;
            Gson jason = new Gson();

            listContacts = Arrays.asList(jason.fromJson(result, Contact[].class));

            return listContacts;
        }

        @Override
        protected void onPostExecute(String s) {
            reloadListener.onContactsSync(listContacts);
        }
    }

    public void receiveContacts(ReloadContacts reloadListener){
        ReceiveContacts receive = new ReceiveContacts(reloadListener);
        receive.execute();
    }


    public void sendContacts(String name, String phone){
        SendContacts sendContacts = new SendContacts(name, phone);
        sendContacts.execute();
    }

    public String postContact(String name, String phone) throws IOException {
        OutputStreamWriter writer = null;
        String contentAsString = "";
        try {
            URL url = new URL(CONTACTS_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* miliseconds */);
            conn.setConnectTimeout(15000 /* miliseconds */);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            JSONObject data = new JSONObject();
            data.put("name", name);
            data.put("phone", phone);
            JSONObject contact = new JSONObject();
            contact.put("contact", data);

            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(contact.toString());
            writer.flush();

            StringBuilder builder = new StringBuilder();
            int httpResult = conn.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_CREATED || httpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                reader.close();
                contentAsString = builder.toString();
            }
            else {
                contentAsString = conn.getResponseMessage();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }
        return contentAsString;
    }

    public String getContacts() throws IOException {
        OutputStreamWriter writer = null;
        String contentAsString = "";
        try {
            URL url = new URL(CONTACTS_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* miliseconds */);
            conn.setConnectTimeout(15000 /* miliseconds */);
            conn.setRequestProperty("Content-length", "0");
            conn.setRequestMethod("GET");
            conn.setAllowUserInteraction(false);
            conn.setUseCaches(false);
            conn.connect();

            StringBuilder builder = new StringBuilder();
            int httpResult = conn.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_CREATED || httpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                reader.close();
                contentAsString = builder.toString();
            }
            else {
                contentAsString = conn.getResponseMessage();
            }
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }

        return contentAsString;
    }
}
