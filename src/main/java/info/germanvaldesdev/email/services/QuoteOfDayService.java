package info.germanvaldesdev.email.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class QuoteOfDayService {

    public String getQuoteOfTheDay() throws IOException {
        URL url = new URL("https://quotes.rest/qod");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) con.getContent()));
        String quote = root.getAsJsonObject().getAsJsonObject("contents")
                .getAsJsonArray("quotes")
                .get(0).getAsJsonObject()
                .get("quote").getAsString();
        return quote;
    }
}
