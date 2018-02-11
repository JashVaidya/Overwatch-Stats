/**
 *
 * OverwatchStats runs the web scraper that retrieves a user's skill data from Overwatch
 * @author Jash Vaidya
 * @version 1.0
 */

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class OverwatchStats {

    public static void main(String[] args) throws Exception {

        //Opens the file with the usernames
        File file = new File("/Users/jash/Desktop/users.txt");
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        List<String> list = new ArrayList<String>();
        while ((str = in.readLine()) != null) {
            list.add(str);
        }

        //Adds each username into an array
        String[] users = list.toArray(new String[0]);

        //Writes out the various information to a text file
        PrintWriter writer = new PrintWriter("Player-Info.txt", "UTF-8");

        for (int j = 0; j < users.length; j++) {
            //To show the time and date of accessing data
            String timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());

            //Getting the user's profile from the website overbuff.com that has a collection of user data
            Document doc = Jsoup.connect("https://www.overbuff.com/players/psn/" + users[j]).userAgent("Mozilla/17.0").get();
            try {

                //Stores the user's data into Elements to be accessed
                Elements userName = doc.select("div.layout-header-primary-bio");
                Elements skillRank = doc.select("div.layout-header-secondary");
                Elements heroes = doc.select("div.name");
                Elements stats = doc.select("tbody.stripe-rows");
                Elements level = doc.select("div.image-with-corner");

                //Converts the user's data from Elements into the appropriate data type and stores it
                String playerLvl = level.get(0).getElementsByClass("corner-text").first().text();
                ArrayList<String> heroArr = new ArrayList<>();
                ArrayList<String> statArr = new ArrayList<>();
                for (int i = 0; i < heroes.size(); i++) {
                    heroArr.add(heroes.get(i).getElementsByTag("a").first().text());
                }
                for (int i = 1; i < stats.size(); i++) {
                    statArr.add(stats.get(i).getElementsByTag("tr").first().text());
                }

                String user = userName.get(0).getElementsByTag("h1").first().text();
                user = user.substring(0, user.length() - 3);
                String skill = skillRank.get(0).getElementsByTag("span").first().text();
                String fire = skillRank.get(0).getElementsByTag("dd").get(1).text();

                //Prints out the user's data to the text file
                writer.println("Username: " + user);
                writer.println("Player level: " + playerLvl);
                writer.println("Skill Rating: " + skill);
                writer.println("On Fire: " + fire);
                writer.println("Date/Time Accessed: " + timeStamp);

                writer.println("Top 5 Played Heroes: ");
                for (int k = 0; k < 5; k++) {
                    writer.println("\t" + (k + 1) + ". " + heroArr.get(k));
                }
                writer.println("General Stats: ");
                for (int i = 0; i < statArr.size(); i++) {
                    writer.println("\t" + statArr.get(i));
                }
                writer.println("-----------------------------------------");
                writer.println();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        writer.close();

    }
}
