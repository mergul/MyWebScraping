package org.example.webpagestests;

import com.google.common.collect.ImmutableMap;
import org.example.base.Content;
import org.example.base.CustomWebListener;
import org.example.webpages.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.*;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class DevTest {

    private static final Logger log = LoggerFactory.getLogger(DevTest.class);
    public static WebDriver driver;
   //  public static EventFiringWebDriver eventFiringWebDriver;
   //  public static WebEventListener eventListener;
    public static WebDriver decorated;
    public static CustomWebListener eventListener;

    @Before
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        Map<String, Object> prefs = new HashMap<>();
//        prefs.put("intl.accept_languages", "tr");

        Map<String, Object> langs = new HashMap<>();
        langs.put("tr", "en");
        langs.put("fr", "en");
        langs.put("de", "en");
        prefs.put("translate_whitelists", langs);

        Map<String, Boolean> map= ImmutableMap.of("enabled", true);
        prefs.put("translate", map);

//        prefs.put("plugins.plugins_disabled", new String[] { "Chrome PDF Viewer" });
//        prefs.put("plugins.always_open_pdf_externally", true);
//        prefs.put("download.default_directory", "/tmp");//System.getProperty("user.dir")+ File.separator + "externalFiles" + File.separator + "downloadFiles"

        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.DRIVER, Level.ALL);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("goog:loggingPrefs", logs);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        options.setExperimentalOption("prefs", prefs);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        options.merge(capabilities);
        driver = new ChromeDriver(options);
        eventListener = new CustomWebListener();
        EventFiringDecorator<WebDriver> decorator = new EventFiringDecorator<>(eventListener);
        decorated = decorator.decorate(driver);
        decorated.manage().window().maximize();
        decorated.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
       // eventFiringWebDriver = new EventFiringWebDriver(driver);
       // eventListener = new WebEventListener();
       // eventFiringWebDriver.register(eventListener);
       // eventFiringWebDriver.manage().window().maximize();
       // eventFiringWebDriver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
    }

    @Test
    public void applyCovidUpload() throws InterruptedException {

        WikiCovidPage wikiCovidPage = WikiCovidPage.go(decorated);
        String table = wikiCovidPage.getTableHtml();

//        LemondePage lemondePage = LemondePage.go(decorated);
//        String svg = lemondePage.getSvgHtml();

//        KoronaPage koronaPage = KoronaPage.go(decorated);
//        String img = koronaPage.getImage();
//        koronaPage.findImg();

        AmnestyFeed amnesty = AmnestyFeed.go(decorated);
        List<Content> articles = amnesty.getArticles();

//        YoutubePage youtubePage= YoutubePage.go(decorated,"Lockdown TV");
//        YoutubeEmbedPage embedPage=youtubePage.getEmbeddedVideo();
//        String ima=embedPage.getEmbedCode();

        SignPage signPage = SignPage.go(decorated);
        LoginPage loginPage = signPage.login();
        ProfilePage profilePage = loginPage.loginMe("ergul_mesut@yahoo.com", "Salacak123");

        UploadPage uploadPage = profilePage.goUpload();
        profilePage = uploadPage.uploadContent(articles.get(1).getTitle(), articles.get(1).getInner(), List.of(table), List.of(), "", List.of());
        Thread.sleep(2000L);
    }

    @Test
    public void applyLogin() throws InterruptedException {
        GooPicsPage picsPage = GooPicsPage.go(decorated, "Iran: Tortured Iranian Kurdish man sentenced to death: Reza Rasaei");
        List<String> allHref=picsPage.findMyRawImages();
        log.info("allHref -> "+allHref.toString());
//        SignPage signPage = SignPage.go(decorated);
//        LoginPage loginPage = signPage.login();
//       // LoginPage loginPage=LoginPage.go(decorated);
//        ProfilePage page = loginPage.loginMe("ergul_mesut@yahoo.com", "Salacak123");
//        UploadPage uploadPage= page.goUpload();
//        uploadPage.uploadBase(allHref);
       // Thread.sleep(5000L);
    }

    @Test
    public void applyDownloadHtml() {
        DownloadHtmlPage downloadHtmlPage = DownloadHtmlPage.go(decorated, "file:////tmp/mifrance3.html");
        String table=downloadHtmlPage.getContentItems();
        log.info(table.substring(1, 10));
    }
    @Test
    public void applyDownHtml() {
        DownloadHtmlPage downloadHtmlPage = DownloadHtmlPage.go(decorated, "file:////tmp/mifrance3.html");
        downloadHtmlPage.transformTableau();
    }
    @Test
    public void applyDownloadImgs() {
        String img = DownloadImgPage.getImage("/tmp/saglik.png");
        log.info(img.substring(1, 100));
    }

    @Test
    public void applyWiki() {
        WikiCovidPage wikiCovidPage = WikiCovidPage.go(decorated);
        wikiCovidPage.downloadTableHtml();
    }

    @Test
    public void applyKoronaUpload() {
        Logs logs = decorated.manage().logs();
        LogEntries logEntries = logs.get(LogType.DRIVER);

        for (LogEntry logEntry : logEntries) {
            log.info(logEntry.getMessage());
        }
        KoronaPage koronaPage = KoronaPage.go(decorated);
        koronaPage.findImg();
        String ima=koronaPage.getFile();
        log.info(ima.substring(1, 10));
    }

    @Test
    public void applyAmnestyUpload() throws InterruptedException, IOException {
        YoutubePage youtubePage= YoutubePage.go(decorated,"gigi");
        YoutubeEmbedPage embedPage=youtubePage.getEmbeddedVideo();
        String embedCode=embedPage.getEmbedCode();
        AmnestyFeed amnesty = AmnestyFeed.go(decorated);
        List<Content> articles = amnesty.getArticles();
        List<Content> details=amnesty.getDetails();
        Map<Integer, List<String>> listMap=new HashMap<Integer, List<String>>();
        for (int i=0; i<3; i++) {
            Content article = articles.get(i);
            Content detail = details.get(i);
            String con=detail.getInner();
            String cont=article.getInner();
            log.info("GTranslate detail -> "+con);
            StringBuilder translated= new StringBuilder();
            String txt=article.getTitle()+" <> "+cont+" <> "+con;
            String[] stooges = txt.split("(?<=\\G.{4999})");
            for (String myTxt: stooges) {
                TranslatedPage translatedPage = GTranslate.go(decorated, "en", "tr",myTxt);
                translated.append(translatedPage.getTextOf());
                Thread.sleep(2L);
            }
            article.setTranslated(String.valueOf(translated));
            log.info("GTranslate -> "+article.getTranslated());
            GooPicsPage picsPage = GooPicsPage.go(decorated, article.getTitle());
            List<String> allHref=picsPage.findImagesBase64(article.getTitle().substring(0, Math.min(article.getTitle().length(), 20)).replace("/", "").trim());
            log.info(allHref.toString());
            List<String> externalUrls = new ArrayList<>(Collections.unmodifiableList(picsPage.findMyRawImages()));
            listMap.put(i, externalUrls);
            log.info("externalUrls -> "+ externalUrls);
            article.getObjects().addAll(Collections.unmodifiableList(allHref));
        }
//        for (Content content : articles) {
//            GooPicsPage picsPage = GooPicsPage.go(decorated, content.getTitle());
//            List<String> allHref=picsPage.findImages();
//            content.getHref().addAll(Collections.unmodifiableList(allHref));
//        }
        SignPage signPage = SignPage.go(decorated);
        LoginPage loginPage = signPage.login();
//        ProfilePage profilePage = loginPage.loginMe("ergul_mesut@yahoo.com", "Salacak123");
        ProfilePage profilePage = loginPage.loginMe("ergul_mesut@hotmail.com", "Merina75");
        for (int i=0; i<3; i++) {
            Content article = articles.get(i);
            UploadPage uploadPage = profilePage.goUpload();
            String[] myStrings=article.getTranslated().split("<>");
            log.info("translated details-> "+ Arrays.toString(myStrings));
            profilePage = uploadPage.uploadContent(myStrings.length == 1 ? "empty topic"+" #gigi" : myStrings[0]+" #gigi", myStrings.length > 1 ? myStrings[1] : myStrings[0], listMap.get(i), article.getObjects(), embedCode, details.get(i).getObjects());
           // profilePage.isUploaded();
            Thread.sleep(3000L);
        }
    }
    @Test
    public void applyYoutubeEmbed() throws InterruptedException {
        TimerTask mitimer = new TimerTask() {
            @Override
            public void run() {
                YoutubePage youtubePage= YoutubePage.go(decorated,"Iranian Kurdish woman");
                YoutubeEmbedPage embedPage=youtubePage.getEmbeddedVideo();
                String ima=embedPage.getEmbedCode();
                log.info(ima);
            }
        };
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        long delay = 5000L;
        long period = 1000L * 60L * 60L * 24L;
        executor.scheduleAtFixedRate(mitimer, delay, period, TimeUnit.MICROSECONDS);
        Thread.sleep(20000L);
    }
    @Test
    public void applyNPR() {
        NprPage nprPage = NprPage.go(decorated);
        Content content=nprPage.getNews();
        log.info(content.getInner());
    }
    @Test
    public void applyGooImages() {
        GooPicsPage picsPage = GooPicsPage.go(decorated, "Iran: Further Information: Iranian Kurdish woman denied medical care: Zeynab Jalalian");
        picsPage.downloadImgs();
    }
    // @Test
    // public void applyGooTranslate() {
    //     GooTranslate gooTranslate = GooTranslate.go(decorated);
    //     TranslatedPage translatedPage = gooTranslate.translateText("How do you do, Sir?");
    //     translatedPage.printOut();
    // }
    @Test
    public void applyGTranslate() {
        TranslatedPage translatedPage = GTranslate.go(decorated, "en", "tr", "How do you do, Sir?");
        translatedPage.printOut();
    }
    @Test
    public void applyDW() {
        DeutPage deutPage = DeutPage.go(decorated);
        List<Content> contents=deutPage.getArticles();
        log.info(contents.get(0).getInner());
    }
    @Test
    public void applyIndy() {
        IndyPage indyPage = IndyPage.go(decorated);
        List<Content> contents=indyPage.getArticles();
        log.info(contents.get(0).getInner());
       // Thread.sleep(1000L);
    }
    @Test
    public void applyJazeera() {
        AljazeeraPage aljazeeraPage = AljazeeraPage.go(decorated);
        List<Content> contents=aljazeeraPage.getArticles();
        log.info(contents.get(0).getInner());
       // Thread.sleep(1000L);
    }
    @Test
    public void applyBrit() {
        BbcPage bbcPage = BbcPage.go(decorated);
        List<Content> contents= bbcPage.getArticles();
        log.info(contents.get(0).getInner());
     //   Thread.sleep(10000L);
    }
    @Test
    public void applyDemocracy() {
        DemoNow demoNow = DemoNow.go(decorated);
        List<Content> contents=demoNow.getArticles();
        log.info(contents.get(0).getInner());
    }
    @Test
    public void applyDownloadLemondePageHtml() {
        LemondePage lemondePage = LemondePage.go(decorated);
        lemondePage.downloadTableHtml();
    }
    @Test
    public void applyDemoDetailsHtml() {
        DemoDetails demoDetails = DemoDetails.go(decorated,"https://www.democracynow.org/2020/6/5/stuart_schrader_police_militarization");
        Content content = demoDetails.checkHtml();
        log.info("Details Page --> "+content.getInner());
    }
    @Test
    public void applyIntelliNews() {
        IntelliNewsPage intelliNewsPage = IntelliNewsPage.go(decorated);
        List<Content> contents=intelliNewsPage.checkHtml();
        for (Content content: contents) {
            IntelliDetails details= IntelliDetails.go(decorated, content.getObjects().get(0));
            Content co = details.checkHtml();
            log.info("Details Page --> "+co.getInner());
        }
    }
    @Test
    public void applyGNews() throws IOException, InterruptedException {
        GoogleNews googleNews = GoogleNews.go(decorated);
        List<Content> contents=googleNews.getArticles();
        List<Content> details=googleNews.getDetails();

        log.info("applyGNews contents -> "+contents.get(0).getInner());
        log.info("applyGNews details -> "+details.get(0).getInner());
    }
    @Test
    public void applyWikiPics() throws InterruptedException {
        Logs logs = decorated.manage().logs();
        LogEntries logEntries = logs.get(LogType.DRIVER);

        for (LogEntry logEntry : logEntries) {
            log.info(logEntry.getMessage());
        }
        WikiPicsPage wikiPicsPage = WikiPicsPage.go(decorated, "https://en.wikipedia.org/wiki/Pierre_Nkurunziza");
        String content=wikiPicsPage.getImage();
        log.info(content);
        Thread.sleep(10000L);
    }
    @After
    public void close() {
        decorated.close();
    }
}
