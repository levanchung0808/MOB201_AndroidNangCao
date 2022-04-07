package com.example.mob201_levanchung_ps19319_asm.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.mob201_levanchung_ps19319_asm.R;
import com.example.mob201_levanchung_ps19319_asm.adapter.NewsAdapter;
import com.example.mob201_levanchung_ps19319_asm.database.DbHelper;
import com.example.mob201_levanchung_ps19319_asm.database.XMLDOMParser;
import com.example.mob201_levanchung_ps19319_asm.model.TinTuc;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsFragment extends Fragment {

    ListView listViewNews;
    ArrayList<TinTuc> arrTinTuc;
    DbHelper dbHelper;
    NewsAdapter adapter;
    SearchView svRSSFeed;
    ImageView ivMic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        listViewNews = view.findViewById(R.id.listViewNews);
        svRSSFeed = view.findViewById(R.id.svRSSFeed);
        ivMic = view.findViewById(R.id.ivMic);
        arrTinTuc = new ArrayList<>();
        dbHelper = new DbHelper(getActivity());

        adapter = new NewsAdapter(arrTinTuc, getActivity(),dbHelper);
        listViewNews.setAdapter(adapter);

        new ReadRSS().execute("https://vnexpress.net/rss/tin-xem-nhieu.rss");

        svRSSFeed.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        ivMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent,200);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200 && resultCode == RESULT_OK){
            ArrayList<String> arrQuery = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String voice = arrQuery.get(0);
            svRSSFeed.setQuery(voice,false);
        }else{
            Toast.makeText(getActivity(), "Voice rỗng", Toast.LENGTH_SHORT).show();
        }
    }

    private class ReadRSS extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }
                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            XMLDOMParser parser = new XMLDOMParser();

            Document document = parser.getDocument(s);

            NodeList nodeList = document.getElementsByTagName("item");

            String title = "", link = "", date = "", img_src = "";

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                title = parser.getValue(element, "title");
                link = parser.getValue(element, "link");
                date = parser.getValue(element, "pubDate");
                img_src = parser.getValue(element, "description");
                Matcher matcher = Pattern.compile("<img src=\"([^\"]+)").matcher(img_src);

                String url = "";
                while (matcher.find()) {
                    url = matcher.group(1);
                    Log.e("src",matcher.group(1));
                }
                TinTuc tinTuc = new TinTuc(title, link, date, url);
                arrTinTuc.add(tinTuc);
            }
            adapter.notifyDataSetChanged();

        }
    }
}