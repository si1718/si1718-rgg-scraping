package web.service;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import web.scraping.jsoup.JsoupScholar;
import web.scraping.jsoup.vo.ImgInfo;

@RestController
public class ServingLayerController {

	@RequestMapping("/scholar")
	public ImgInfo serve(@RequestParam(value = "id", required = true, defaultValue = "null") String tag)
			throws JsonParseException, JsonMappingException, IOException {
		return JsoupScholar.getImgInfoFromGoogleScholar(tag);
	}

}