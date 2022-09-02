package org.example.webpagestests;

import com.google.common.collect.ImmutableMap;
import org.example.base.Content;
import org.example.base.WebEventListener;
import org.example.webpages.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.*;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class DevTest {

    private static final Logger log = LoggerFactory.getLogger(DevTest.class);
    public static WebDriver driver;
    public static EventFiringWebDriver eventFiringWebDriver;
    public static WebEventListener eventListener;

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

        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.DRIVER, Level.ALL);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);

        options.setExperimentalOption("prefs", prefs);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        options.merge(capabilities);
        driver = new ChromeDriver(options);
        eventFiringWebDriver = new EventFiringWebDriver(driver);
        eventListener = new WebEventListener();
        eventFiringWebDriver.register(eventListener);
        eventFiringWebDriver.manage().window().maximize();
        eventFiringWebDriver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
    }

    @Test
    public void applyCovidUpload() throws InterruptedException {

        WikiCovidPage wikiCovidPage = WikiCovidPage.go(eventFiringWebDriver);
        String table = wikiCovidPage.getTableHtml();

        LemondePage lemondePage = LemondePage.go(eventFiringWebDriver);
        String svg = lemondePage.getSvgHtml();

        KoronaPage koronaPage = KoronaPage.go(eventFiringWebDriver);
        String img = koronaPage.getImage();
        koronaPage.findImg();

        AmnestyFeed amnesty = AmnestyFeed.go(eventFiringWebDriver);
        List<Content> articles = amnesty.getArticles();

        YoutubeEmbedPage embedPage = YoutubeEmbedPage.go(eventFiringWebDriver, "Lockdown TV");
        String ima=embedPage.getEmbeddedVideos();

        LoginPage loginPage = LoginPage.go(eventFiringWebDriver);
        ProfilePage profilePage = loginPage.loginMe("ergul_mesut@yahoo.com", "Salacak123");

        UploadPage uploadPage = profilePage.goUpload();
        profilePage = uploadPage.uploadContent(articles.get(1).getTitle(), articles.get(1).getInner(), table, Arrays.asList(img,ima), svg, "/tmp/saglik.png");
        Thread.sleep(2000L);
    }

    @Test
    public void applyLogin() throws InterruptedException {
        Logs logs = eventFiringWebDriver.manage().logs();
        LogEntries logEntries = logs.get(LogType.DRIVER);

        for (LogEntry logEntry : logEntries) {
            log.info(logEntry.getMessage());
        }
        LoginPage loginPage = LoginPage.go(eventFiringWebDriver);
        ProfilePage page = loginPage.loginMe("ergul_mesut@yahoo.com", "Salacak123");
        Thread.sleep(100000L);
    }

    @Test
    public void applyDownloadHtml() {
        DownloadHtmlPage downloadHtmlPage = DownloadHtmlPage.go(eventFiringWebDriver, "file:////tmp/mifrance3.html");
        String table=downloadHtmlPage.getContentItems();
        log.info(table.substring(1, 10));
    }
    @Test
    public void applyDownHtml() {
        DownloadHtmlPage downloadHtmlPage = DownloadHtmlPage.go(eventFiringWebDriver, "file:////tmp/mifrance3.html");
        downloadHtmlPage.transformTableau();
    }
    @Test
    public void applyDownloadImgs() {
        String img = DownloadImgPage.getImage("/tmp/saglik.png");
        log.info(img.substring(1, 100));
    }

    @Test
    public void applyWiki() {
        WikiCovidPage wikiCovidPage = WikiCovidPage.go(eventFiringWebDriver);
        wikiCovidPage.downloadTableHtml();
    }

    @Test
    public void applyKoronaUpload() {
        Logs logs = eventFiringWebDriver.manage().logs();
        LogEntries logEntries = logs.get(LogType.DRIVER);

        for (LogEntry logEntry : logEntries) {
            log.info(logEntry.getMessage());
        }
        KoronaPage koronaPage = KoronaPage.go(eventFiringWebDriver);
        koronaPage.findImg();
        String ima=koronaPage.getFile();
        log.info(ima.substring(1, 10));
    }

    @Test
    public void applyAmnestyUpload() throws InterruptedException {
        AmnestyFeed amnesty = AmnestyFeed.go(eventFiringWebDriver);
        List<Content> articles = amnesty.getArticles();
        for (Content article : articles) {
            GooTranslate gooTranslate = GooTranslate.go(eventFiringWebDriver);
            TranslatedPage translatedPage = gooTranslate.translateText(article.getTitle()+" <> "+article.getInner());
            article.setTranslated(translatedPage.getTextOf());
        }
        for (Content content : articles) {
            GooPicsPage picsPage = GooPicsPage.go(eventFiringWebDriver, content.getTitle());
            content.getHref().addAll(picsPage.findImages());
        }
        LoginPage loginPage = LoginPage.go(eventFiringWebDriver);
        ProfilePage profilePage = loginPage.loginMe("ergul_mesut@yahoo.com", "Salacak123");
        for (Content article : articles) {
            UploadPage uploadPage = profilePage.goUpload();
            String[] myStrings=article.getTranslated().split("<>");
            profilePage = uploadPage.uploadContent(myStrings.length == 1 ? "empty topic" : myStrings[0], myStrings.length > 1 ? myStrings[1] : myStrings[0], "", article.getHref(), "", "/tmp/saglik.png");
            profilePage.isUploaded();
            Thread.sleep(2000L);
        }
    }
    @Test
    public void applyYoutubeEmbed() {
        TimerTask mitimer = new TimerTask() {
            @Override
            public void run() {
                YoutubeEmbedPage embedPage = YoutubeEmbedPage.go(eventFiringWebDriver, "George Floyd Murder");
                String ima=embedPage.getEmbeddedVideos();
                log.info(ima);
            }
        };
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        long delay = 5000L;
        long period = 1000L * 60L * 60L * 24L;
        executor.scheduleAtFixedRate(mitimer, delay, period, TimeUnit.MICROSECONDS);
    }
    @Test
    public void applyNPR() {
        NprPage nprPage = NprPage.go(eventFiringWebDriver);
        Content content=nprPage.getNews();
        log.info(content.getInner());
    }
    @Test
    public void applyGooImages() {
        GooPicsPage picsPage = GooPicsPage.go(eventFiringWebDriver, "Clinton");
        picsPage.downloadImgs();
    }
    @Test
    public void applyGooTranslate() {
        GooTranslate gooTranslate = GooTranslate.go(eventFiringWebDriver);
        TranslatedPage translatedPage = gooTranslate.translateText("How do you do, Sir?");
        translatedPage.printOut();
    }
    @Test
    public void applyDW() {
        DeutPage deutPage = DeutPage.go(eventFiringWebDriver);
        List<Content> contents=deutPage.getArticles();
        log.info(contents.get(0).getInner());
    }
    @Test
    public void applyIndy() throws InterruptedException {
        IndyPage indyPage = IndyPage.go(eventFiringWebDriver);
        List<Content> contents=indyPage.getArticles();
        log.info(contents.get(0).getInner());
        Thread.sleep(100000L);
    }
    @Test
    public void applyBrit() throws InterruptedException {
        BbcPage bbcPage = BbcPage.go(eventFiringWebDriver);
        List<Content> contents= bbcPage.getArticles();
        log.info(contents.get(0).getInner());
        Thread.sleep(100000L);
    }
    @Test
    public void applyDemocracy() {
        DemoNow demoNow = DemoNow.go(eventFiringWebDriver);
        List<Content> contents=demoNow.getArticles();
        log.info(contents.get(0).getInner());
    }
    @Test
    public void applyDownloadLemondePageHtml() {
        LemondePage lemondePage = LemondePage.go(eventFiringWebDriver);
        lemondePage.downloadTableHtml();
    }
    @Test
    public void applyDemoDetailsHtml() {
        DemoDetails demoDetails = DemoDetails.go(eventFiringWebDriver,"https://www.democracynow.org/2020/6/5/stuart_schrader_police_militarization");
        Content content = demoDetails.checkHtml();
        log.info("Details Page --> "+content.getInner());
    }
    @Test
    public void applyIntelliNews() {
        IntelliNewsPage intelliNewsPage = IntelliNewsPage.go(eventFiringWebDriver);
        List<Content> contents=intelliNewsPage.checkHtml();
        for (Content content: contents) {
            IntelliDetails details= IntelliDetails.go(eventFiringWebDriver, content.getHref().get(0));
            Content co = details.checkHtml();
            log.info("Details Page --> "+co.getInner());
        }
    }
    @Test
    public void applyWikiPics() throws InterruptedException {
        Logs logs = eventFiringWebDriver.manage().logs();
        LogEntries logEntries = logs.get(LogType.DRIVER);

        for (LogEntry logEntry : logEntries) {
            log.info(logEntry.getMessage());
        }
        WikiPicsPage wikiPicsPage = WikiPicsPage.go(eventFiringWebDriver, "https://en.wikipedia.org/wiki/Pierre_Nkurunziza");
        String content=wikiPicsPage.getImage();
        log.info(content);
        Thread.sleep(100000L);
    }
    @After
    public void close() {
        eventFiringWebDriver.close();
    }
}
