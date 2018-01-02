package web.scraping.jsoup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import web.scraping.jsoup.vo.ImgInfo;

public class JsoupScholar {
	private static final String URL_BASE = "https://scholar.google.com/";
	private static final String URL_SCHOLAR = URL_BASE + "citations?user=";

	public static ImgInfo getImgInfoFromGoogleScholar(String id) throws MalformedURLException, IOException {

		// String idPablo = "y2qDI6IAAAAJ";
		// String idJorge = "O719x-wAAAAJ";

		Document doc = Jsoup.parse(new URL(URL_SCHOLAR + id), 1000);

		ImgInfo aux = new ImgInfo();

		aux.setSrc(doc.getElementsByAttributeValue("id", "gsc_prf_pup-img").attr("src"));

		return aux;
	}

}
