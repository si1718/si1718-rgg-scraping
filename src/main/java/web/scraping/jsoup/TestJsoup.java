package web.scraping.jsoup;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteResult;



public class TestJsoup {

	private static final String GROUPS_URL = "https://investigacion.us.es/investigacion/sisius/grupos";
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


	public static List<String> phoneNumbers (String id_group, int l, Element t, String cad_phone) {
		List <String> phoneList = new ArrayList <String> ();
		String cad_phone1, cad_phone2, cad_phone3, cad_phone4;

		for (int o = 0; o < cad_phone.length(); o++){ // Quitar puntos, guiones y espacios entre números
			cad_phone = cad_phone.replace(".", "");
			cad_phone = cad_phone.replace(" ", "");
			cad_phone = cad_phone.replace("-", "");
			cad_phone = cad_phone.replace("(", "");
			cad_phone = cad_phone.replace(")", "");
			cad_phone = cad_phone.replace(":", "");
		}

		if (cad_phone.contains("</li>")) { cad_phone = cad_phone.substring(0, cad_phone.length()-5); }

		if (cad_phone.length() > 9 && cad_phone.toString().indexOf("+34") != -1) {
			cad_phone1 = cad_phone.substring(3, 12);
			phoneList.add(cad_phone1);
			cad_phone2 = cad_phone.substring(3, 6) + cad_phone.substring(15, 21);
			phoneList.add(cad_phone2);
		}
		else if (cad_phone.length() > 9 && cad_phone.toString().indexOf("(ext") != -1) {
			cad_phone1 = cad_phone.substring(0, cad_phone.length()-10);
			phoneList.add(cad_phone1);
			cad_phone2 = cad_phone.substring(0, 7) + cad_phone.substring(9, 11);
			phoneList.add(cad_phone2);
		}
		else if (cad_phone.length() > 9 && cad_phone.toString().indexOf("(Ext") != -1) {
			phoneList.add(cad_phone.substring(0, 9));
		}
		else if (cad_phone.length() == 16 && cad_phone.toString().indexOf("0ext") != -1) {
			phoneList.add(cad_phone.substring(0, 9));
			cad_phone2 = cad_phone.substring(0, 5) + cad_phone.substring(12, 16);
			phoneList.add(cad_phone2);
		}
		else if (cad_phone.length() > 9 && cad_phone.toString().indexOf("/") != -1) {
			phoneList.add(cad_phone.toString().split("/")[0]);
			if (cad_phone.length() == 11) {
				cad_phone2 = cad_phone.substring(0, 8) + cad_phone.substring(10, 11);
				phoneList.add(cad_phone2);
			}
			else if (cad_phone.length() == 12) {
				cad_phone2 = cad_phone.substring(0, 7) + cad_phone.substring(10, 12);
				phoneList.add(cad_phone2);
			}
			else if (cad_phone.length() == 14) {
				cad_phone2 = cad_phone.substring(0, 5) + cad_phone.substring(10, 14);
				phoneList.add(cad_phone2);
			}
			else if (cad_phone.length() == 15) {
				cad_phone2 = cad_phone.substring(0, 4) + cad_phone.substring(10, 15);
				phoneList.add(cad_phone2);
			}
			else if (cad_phone.length() == 16) {
    				cad_phone2 = cad_phone.substring(0, 3) + cad_phone.substring(10, 16);
    				phoneList.add(cad_phone2);
    			}
			else if (cad_phone.length() == 17) {
				cad_phone2 = cad_phone.substring(0, 2) + cad_phone.substring(10, 17);
				phoneList.add(cad_phone2);
			}
    			else if (cad_phone.length() == 19) {
    				if(cad_phone.toString().split("/")[0].length() == 9 &&
    				   cad_phone.toString().split("/")[1].length() == 9) {
    					phoneList.add(cad_phone.toString().split("/")[1]);
    				}
    				else if (cad_phone.toString().split("/")[1].length() == 2) {
    					cad_phone2 = cad_phone.substring(0, 7) + cad_phone.substring(10, 12);
        				phoneList.add(cad_phone2);
        				cad_phone3 = cad_phone.substring(0, 3) + cad_phone.substring(13, 19);
        				phoneList.add(cad_phone3);
    				}
    				else{
	    				cad_phone2 = cad_phone.substring(0, 7) + cad_phone.toString().split("/")[1];
	    				phoneList.add(cad_phone2);
	    				cad_phone3 = cad_phone.substring(0, 7) + cad_phone.substring(13, 15);
	    				phoneList.add(cad_phone3);
	    				cad_phone4 = cad_phone.substring(0, 5) + cad_phone.substring(15, 19);
	    				phoneList.add(cad_phone4);
    				}
    			}
    			else if (cad_phone.length() == 21) {
    				cad_phone2 = cad_phone.substring(9, 18);
    				phoneList.add(cad_phone2);
    				cad_phone3 = cad_phone.substring(9, 16) + cad_phone.substring(19, 21);
    				phoneList.add(cad_phone3);
    			}
    			else if (cad_phone.length() == 22) {
    				cad_phone2 = cad_phone.substring(10, 19);
    				phoneList.add(cad_phone2);
    				cad_phone3 = cad_phone.substring(10, 17) + cad_phone.substring(20, 22);
    				phoneList.add(cad_phone3);
    			}
    			else{
	    			phoneList.add(cad_phone.toString().split("/")[1]);
    			}
		}
		else if (cad_phone.length() == 10 || cad_phone.length() == 13 || cad_phone.length() == 14
				|| cad_phone.length() == 16 || cad_phone.length() == 18) {
			cad_phone1 = cad_phone.substring(0, 9);
			phoneList.add(cad_phone1);

			if (cad_phone.length() == 10) {
				cad_phone2 = cad_phone.substring(0, 8) + cad_phone.substring(9, 10);
				phoneList.add(cad_phone2);
			}
			else if (cad_phone.length() == 13) {
				cad_phone2 = cad_phone.substring(0, 7) + cad_phone.substring(9, 11);
				phoneList.add(cad_phone2);
				cad_phone3 = cad_phone.substring(0, 7) + cad_phone.substring(11, 13);
				phoneList.add(cad_phone3);
			}
			else if (cad_phone.length() == 14) {
				cad_phone2 = cad_phone.substring(0, 4) + cad_phone.substring(9, 14);
				phoneList.add(cad_phone2);
			}
			else if (cad_phone.length() == 16) {
				cad_phone2 = cad_phone.substring(0, 2) + cad_phone.substring(9, 16);
				phoneList.add(cad_phone2);
			}
			else{
				cad_phone2 = cad_phone.substring(9, 18);
				phoneList.add(cad_phone2);
			}
		}
		else{
			phoneList.add(cad_phone);
		}

		return phoneList;
	}


	public static List<String> lines (Element e) {
		List<String> res = new ArrayList <String> ();

		for (int n = 0; n < e.children().size(); n++) {
			String line = e.child(n).text();
			res.add(line);
		}

		return res;
	}


	public static void createKeywordsAreaName (List <String> keywords, String area_name, String group_name) {
		Set <String> stringsSet = new HashSet <> ();

		//List <String> words = Arrays.asList("activid", "actual", "adultas", "ambito", "acuatica", "andalus", "andalusi", "andaluces", "andaluza", "andaluzas", "andaluz", "anillos", "animal", "antiqua.", "antonio", "aplicacion", "aplicado", "applied", "archivos", "architecture", "artistico", "atlas.", "automatica", "automatico", "autonomico", "basicos", "business", "cadena", "calidad", "cambio", "capacidades", "carbolinas", "caries", "carrera", "celiaca", "celulas", "centro", "ceremonial", "cientifica", "cientifico", "circuitos", "ciudad", "ciudadania", "ciudadano", "clasica", "clinica.", "clinicos", "codigos", "cognitiva", "combinatorias", "comparacion", "comparada", "comparados", "competencia", "compleja", "complementaria", "comportamiento", "composite", "comprometidos", "computacion", "compuestos", "computadores", "comunicacion" , "comunicaciones", "comunidades", "comunitaria", "conceptos", "conducta", "conocimiento", "conservacion", "conservados", "construccion", "construcciones", "construido", "contabilidad", "contable", "contemporaneo", "contenido", "contenidos", "contexto", "contextos", "contrato", "control", "controlada", "coordinacion", "cosmeticos", "creacion", "creativa", "crstalinos", "cristobal", "critico", "cualitativo", "cualitativos", "cuantitativos", "ciudad", "cuidados", "cultivo", "cultura", "cultural", "culturales", "culturas", "deinvestigacion", "demanda", "democracia", "dental", "departamento", "deporte", "deportiva", "deportivos", "derecho", "derechos", "derivadas", "derivados", "desarrollo", "desigualdad", "desigualdades", "desigualdades", "determinantes", "development", "diagnostico", "dialogo", "dibujo", "didacticas", "diferencial", "diferenciales", "difuso", "digital", "digitales", "dinamica", "dinamicos", "direccion", "discreta", "discursos", "diseño", "diseños", "distintos", "diversidad", "dicencia", "docente", "documentos", "durante", "economia", "economica", "economicas", "economico", "economicos", "economics", "ecuaciones", "edicion", "educacion", "educativa", "educativos", "ejercicio", "elasticidad", "electrica", "electronica", "emocional", "emotional", "empleo", "emprendedora", "empresa", "empresrial", "endodoncia", "energ.", "energetico", "energias", "enfermedad", "enfermedades", "enseñanza", "enseñanzas", "entrepreneurship", "envejecimiento", "equipos", "escenarios", "escolar", "escolares", "escrita", "escritoras", "escritos", "escrituras", "españa", "español", "especial", "especiales", "estado", "estatal", "estetica", "estrategias", "estres", "estructura", "estructuras", "estudio", "europa", "europe", "europeas", "europeos", "evaluac.", "exercise", "expresion", "extranjera", "extranjeras.", "extranjeros", "factory", "familia", "familiar", "familiares", "feminism", "fenomeno", "fenomenos", "financiera", "financiero", "finanzas", "fisicas", "fisico", "flujos", "fondos", "formacion", "formas", "francisco", "fronteras", "fuentes", "func.del", "funcionales", "fundamental", "gaipij", "generacion", "genero", "generos", "gentes", "geograficos", "gestion", "giecse", "giftedness", "global", "grafica", "grafos", "griega", "grupos", "hablada", "hablado", "healthy", "helenistica", "hermes", "herramientas", "heterogenea", "homogenea", "homogenea.", "homogeneizacion", "homotopia", "hongos", "hospitalaria", "humana", "humanistica", "humano", "humanos", "iberia", "iberica", "iberoamericano", "identidad", "identidades", "idiomas", "iluminacion", "imagen", "imagenan", "imagenes", "impacto", "implantologia", "incertidumbre", "incidencias", "indias", "industria", "industriales", "infantil", "infanto", "informacion", "informatica", "informaticos", "ingles", "inglesa", "ingleses", "innovacion", "innovation", "inocul.", "instalaciones", "institucional", "instituciones", "instituto", "integrada", "integrados", "intelligence", "interaccion", "interculturalidad.", "interdisciplinario", "interes", "internac", "internacionales", "interpersonal", "interpretacion", "interrelacion", "invespot", "investigation", "isidorianum", "italiana", "judicial", "juegos", "juridica", "juridicas", "juridico", "juventud", "laborales", "labour", "latina", "laurea", "lecturas", "legum.", "lengua", "lenguajes", "lenguas", "lenguas.", "levaduras", "lexicon", "libertad.", "limites", "lineales", "linguistica", "linguistico", "lingüisticos", "literarias", "literarios", "literatura", "literaturas", "litesco", "litoral", "local.", "localizacion", "loscertales", "lugar.", "maecei", "management", "marinos",  "matematica", "maxilofac.", "mecanicas", "med_soil", "medicamentos", "medicas", "medioambientales", "medios", "mediterraneo", "mejora", "mercado", "methodologies", "metodologicas", "mixtos", "modelos", "morales", "morfolog.", "motores", "movimiento", "muerte", "mujeres", "municipal", "museum", "musica", "naturacion", "natural", "naturaleza", "network", "nuevas", "nuevos", "obligatorias", "occidental", "operaciones", "operadores", "operativa", "ordenac", "ordenacion", "organizacion", "organizativos.", "organometalicos.", "organos", "origen", "otras", "out_arquias.", "pacientes", "paisaje", "paisajes", "parciales", "patrimonial", "patrimonio", "patrimonio.", "pattern", "penales", "peninsula", "pensamiento", "pensiones", "pequeños", "periodismo", "persona", "personales", "personas", "perspectiva", "pintura", "planes", "planificacion", "planta", "plastica", "plastico", "poesia", "politica", "politicas", "politicos", "popular", "populares", "practica", "prediccion", "privado", "problemas", "procedimientos", "procesos", "produc.", "produccion", "producto", "productos", "profesor", "profesorado", "programas", "progreso", "promocion", "propaganda", "propia", "propiedades", "proteinas", "protocolo", "proyeccion", "proyecto", "proyectos", "publica", "publica.", "publicas", "publico", "quimico", "reacciones", "recepcion", "reciente", "recognition", "recursos", "referencia", "reflexion", "regimen", "regimenes", "regional", "registros", "regulacion", "reguladores", "rehabilitacion", "relac.", "relaciones", "relatos", "religio", "religiones", "religioso", "renovables", "repercusiones", "reproductiva", "research", "residuales", "residuos", "resistencia", "responsabil", "responsabilidad", "responsabilidades", "rodriguez", "romana", "s.o.s.", "saludable", "sanitaria", "secuencial", "seguro", "series", "servicios", "sevilla", "señales", "siglos", "sistema", "sistemas", "situaciones", "sociedades", "stocheion", "sufrimiento", "tecnicas", "tecnolog.", "tecnologias", "tellus.", "tendencias", "terapias", "termicas", "termicos", "terreno", "territ", "territorial", "territorio", "territorios", "territorios]", "territory", "textos", "trabajo", "traduccion", "trafico", "transf.de", "transformaciones", "transmision", "transportes", "tributarios", "turismo", "turista", "turisticos", "unidad", "universidad", "universitario", "urbana", "urbano", "urbanos", "valores", "variable", "velasco", "victimas", "viejos", "vision", "visual", "xix");
		
		area_name = area_name.toLowerCase();
		group_name = group_name.toLowerCase();

		while (group_name.contains("-") || group_name.contains("/") || group_name.contains(":") || group_name.contains(", ")
				|| group_name.contains(" & ") || group_name.contains("(") || group_name.contains(")")
				|| (group_name.contains(".") && !group_name.contains(" .") && !group_name.contains(". "))) {
			group_name = group_name.replace("-", " ").replace("/", " ").replace(":", "").replace(", ", " ").replace(" & ", " ")
								   .replace("(", "").replace(")", "").replace(".", " ");
		}

		while (area_name.contains("á") || area_name.contains("é") || area_name.contains("í") || area_name.contains("ó")
				|| area_name.contains("ú") || group_name.contains("á") || group_name.contains("é") || group_name.contains("í")
				|| group_name.contains("ó") || group_name.contains("ú")) {
			area_name = area_name.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
			group_name = group_name.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
		}
		
		for (int i = 0; i < area_name.split(" ").length; i++) {
			if (area_name.split(" ")[i].contains("biologia")) {
				stringsSet.add("biology");
			}
			else if (area_name.split(" ")[i].contains("biotecnologia")) {
				stringsSet.add("biotech");
			}
			else if (area_name.split(" ")[i].contains("ciencias")) {
				stringsSet.add("science");
			}
			else if (area_name.split(" ")[i].contains("salud")) {
				stringsSet.add("health");
			}
			else if (area_name.split(" ")[i].contains("creacion")) {
				stringsSet.add("creation");
			}
			else if (area_name.split(" ")[i].contains("tecnologias")) {
				stringsSet.add("technology");
			}
			else if (area_name.split(" ")[i].contains("energia")) {
				stringsSet.add("energy");
			}
			else if (area_name.split(" ")[i].contains("humanidades")) {
				stringsSet.add("humanities");
			}
			else {
				stringsSet.add(area_name.split(" ")[i]);
			}
		}

		for (int j = 0; j < group_name.split(" ").length; j++) {
			if (group_name.split(" ")[j].contains("tecnologias") || group_name.split(" ")[j].contains("tecnologia")) {
				stringsSet.add("technology");
			}
			else if (group_name.split(" ")[j].contains("ciencia")) {
				stringsSet.add("science");
			}
			else {
				stringsSet.add(group_name.split(" ")[j]);
			}
		}

		for (String elem : stringsSet) {
			if (elem.toString().length() > 5 /*&& !words.contains(elem)*/
					&& !(elem.matches("[+-]?\\d*(\\.\\d+)?") && elem.equals("")==false)) {
				keywords.add(elem);
			}
		}
		
		keywords.add("tic");
	}
	
	
	public static void webScrapping () throws MalformedURLException, IOException, JSONException {
		Set <DBObject> allDocuments = new HashSet <> (); // This file will contain all the data that will be sent to the database
		int groups = 0; // To see how many groups are in each area

		//List <String> dataResearchers = getDataResearchers();

		/*FOR AREA*/
		Document areas_doc = Jsoup.parse(new URL(GROUPS_URL), 10000);
		Elements elementos = areas_doc.getElementsByTag("a");

		for (int i = 0; i < elementos.size(); i++) {
			String incomplete_area_url = elementos.get(i).attr("href"); // Incomplete url of an area

	        if (incomplete_area_url.indexOf("/sisius/sisius.php?en=6&text2search=") != -1) {
	        		String area_url = "https://investigacion.us.es" + incomplete_area_url; // URL of an area
	        		String area_name = elementos.get(i).ownText(); // Name of an area
	        		/*FOR GROUP + IDGROUP*/
	        		Document groups_doc = Jsoup.parse(new URL(area_url), 10000);
	        		Elements elementos1 = groups_doc.getElementsByAttributeValue("class", "data");
        			int iii = 0;

	        		for (int j = 0; j < elementos1.size(); j++) {
	        			DBObject document = new BasicDBObject();
	        			List <String> keywords = new ArrayList <> ();
	        			/** PUT AREA **/ document.put("scientificTechnicalArea", area_name);
	        			String incomplete_group_url = elementos1.get(j).children().attr("href"); // Incomplete url of an group

	        			if (incomplete_group_url.indexOf("/sisius/sis_depgrupos.php?seltext=") != -1) {
	        				String group_url = "https://investigacion.us.es" + incomplete_group_url; // URL of an area
	        				String group_name_with_id = elementos1.get(j).child(0).ownText(); // Name of an area
	        				String group_name = group_name_with_id.substring(0, group_name_with_id.length()-9);
	        				/** PUT NAME **/ document.put("name", group_name);
	        				createKeywordsAreaName(keywords, area_name, group_name);

		    	        		String id_group = group_name_with_id.substring(group_name_with_id.length()-8, group_name_with_id.length()-1);
		    	        		if (id_group.contains("(")) {
		    	        			id_group = id_group.substring(1, id_group.length());
		    	        		}
		    	        		/** PUT IDGROUP **/ document.put("idGroup", id_group.toLowerCase());

		    	        		/*FOR LEADER + PHONE*/
		    	        		Document group_doc = Jsoup.parse(new URL(group_url), 10000);
		    	        		Elements elementos2 = group_doc.getElementsByTag("p");
		    	        		Element t = elementos2.get(0);
		    	        		String leader = t.child(1).ownText();
		    	        		String leaderName = "";
		    	        		/** PUT LEADER **/ document.put("leader", leader);
		    	        		document.put("leaderName", leaderName);

		    	        		if (t.toString().indexOf("Teléfono") != -1) { // The leader has a phone number
		    	        			List <String> phoneList = new ArrayList <String> ();
		    	        			String cad = t.toString().split("<br>")[1];
		    	        			String cad_phone = cad.toString().split(": ")[1];
		    	        			phoneList = phoneNumbers(id_group, 0, t, cad_phone);
		    	        			/** PUT PHONE **/ document.put("phone", phoneList.get(0));
		    	        		}

		    	        		/*FOR COMPONENTS*/
		    	        		Elements elementos3 = group_doc.getElementsByTag("ul");
		    	        		Elements t1 = elementos3.get(19).children(); // Lists with components
		    	        		List <String> nameComponents = new ArrayList <String> ();

		    	        		for (int l = 0; l < t1.size(); l++) {
		    	        			if (t1.get(l).toString().indexOf("Teléfono") != -1) { // The component has a phone number
		    	        				String name = t1.get(l).toString().split("Teléfono")[0];
		    	        				if (name.contains("href")) {
		    	        					String name1 = name.toString().split(">")[2];
		    	        					name1 = name1.substring(0, name1.length()-3);
		    	        					if (name1.charAt(name1.length()-1) == ' ') { name1 = name1.substring(0, name1.length()-1); }
		    	        					nameComponents.add(name1);
		    	        				}
		    	        				else{
		    	        					String name2 = name.toString().split(">")[1];
		    	        					if (name2.contains(" .")) { name2 = name2.substring(0, name2.length()-3); }
		    	        					if (name2.contains(".")) { name2 = name2.substring(0, name2.length()-2); }
		    	        					if (name2.contains("</li")) { name2 = name2.substring(0, name2.length()-4); }
		    	        					if (name2.contains(" </")) { name2 = name2.substring(0, name2.length()-3); }
		    	        					if (name2.charAt(name2.length()-1) == ' ') { name2 = name2.substring(0, name2.length()-1); }
		    	        					nameComponents.add(name2);
		    	        				}
		    	        			}
		    	        			else{ // The component hasn´t a phone number
		    	        				String name = t1.get(l).toString().split("Email")[0];
		    	        				if (name.contains("href")) {
		    	        					String name1 = name.toString().split(">")[2];
		    	        					name1 = name1.substring(0, name1.length()-3);
		    	        					nameComponents.add(name1);
		    	        				}
		    	        				else{
		    	        					String name2 = name.toString().split(">")[1];
		    	        					if (name2.contains(" .")) { name2 = name2.substring(0, name2.length()-3); }
		    	        					if (name2.contains(".")) { name2 = name2.substring(0, name2.length()-2); }
		    	        					if (name2.contains("</li")) { name2 = name2.substring(0, name2.length()-4); }
		    	        					if (name2.contains(" </")) { name2 = name2.substring(0, name2.length()-3); }
		    	        					if (name2.charAt(name2.length()-1) == ' ') { name2 = name2.substring(0, name2.length()-1); }
		    	        					nameComponents.add(name2);
		    	        				}
		    	        			}
		    	        		}

		    	        		/** PUT COMPONENTS **/ document.put("components", nameComponents);
		    	        		//createKeywords(dataResearchers, nameComponents, keywords);
		    	        		//keywords.addAll(createKeywords(dataResearchers, nameComponents, new HashSet<String>()));

		    	        		Elements elementos4 = group_doc.getElementsByTag("b");
		    	        		List <String> investigationLines = new ArrayList <> ();  	// LINES OF INVESTIGATION
		    	        		List <String> groupActivity = new ArrayList <> (); // GROUP ACTIVITY
		    	        		List <String> generatedTechnology = new ArrayList <> (); // GENERATED TECHNOLOGY

			    	        	if (elementos4.size() == 4) {
			    	        		Element il = elementos3.get(elementos3.size()-1); // Investigation lines
			    	        		investigationLines = lines(il);
			    	        	}

			    	        	if (elementos4.size() == 5) {
			    	        		Element il = elementos3.get(elementos3.size()-2); // Investigation lines
			    	        		investigationLines = lines(il);

			    	        		Element il1 = elementos3.get(elementos3.size()-1); // Group activity
			    	        		groupActivity = lines(il1);
			    	        	}

			    	        	if (elementos4.size() == 6) {
			    	        		Element il = elementos3.get(elementos3.size()-3); // Investigation lines
			    	        		investigationLines = lines(il);

			    	        		Element il1 = elementos3.get(elementos3.size()-2); // Group activity
			    	        		groupActivity = lines(il1);

			    	        		Element il2 = elementos3.get(elementos3.size()-1); // Generated Technology
			    	        		generatedTechnology = lines(il2);
			    	        	}

			    	        	if (!investigationLines.isEmpty()) {
			    	        		/** PUT LINESOFINVESTIGATION **/ document.put("linesOfInvestigation", investigationLines);
			    	        	}

			    	        	if (!groupActivity.isEmpty()) {
			    	        		/** INSERT GROUPACTIVITY **/ document.put("groupActivity-SICcodes", groupActivity);
			    	        	}

			    	        	if (!generatedTechnology.isEmpty()) {
			    	        		/** INSERT GENERATEDTECHNOLOGY **/ document.put("generatedTechnology-SICcodes", generatedTechnology);
			    	        	}
	        			}
	        			document.put("keywords", keywords);
	        			allDocuments.add(document);
	        			iii++;
	        		}
	        		groups += iii;
	        		System.out.println(area_name + ": " + iii + " groups");
	        }
		}

		System.out.println("----------\n" + groups + " documents (groups) will be inserted");

//		for(DBObject obj:allDocuments){
//			System.out.println(obj);
//			System.out.println("********************************************");
//		}

		System.out.println("----------\n" + "There are " + allDocuments.size() + " documents in the list allDocuments");


		// Connection with MONGODB and data insertion
		MongoClientURI uri = new MongoClientURI("mongodb://rafa:rafa@ds159845.mlab.com:59845/si1718-rgg-groups");
		MongoClient client = new MongoClient(uri);
		DB db = client.getDB("si1718-rgg-groups");

		DBCollection collectionGroups = db.getCollection("groups");
		WriteResult documentsRemoved = collectionGroups.remove(new BasicDBObject());
		System.out.println("(GROUPS) Number of documents are deleted: " + documentsRemoved.getN());

		collectionGroups.insert(new ArrayList<>(allDocuments));
		System.out.println("INFORMATION: New documents inserted");

		client.close();
		System.out.println("----------\n" + "FINISHED SUCCESSFULLY");
	}
	
	
	public static void executor() {
		final Runnable beeper = new Runnable () {
			public void run () {
				try {
					webScrapping();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("IOEXCEPTION: " + e);
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		final ScheduledFuture<?> beeperHandle =
				scheduler.scheduleAtFixedRate(beeper, 12, 12, TimeUnit.HOURS);
	}
	
	
	public static void main(String[] args) throws Exception{
		executor();
	}
}
