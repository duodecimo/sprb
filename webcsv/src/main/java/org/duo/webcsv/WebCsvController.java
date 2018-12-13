package org.duo.webcsv;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@RestController
public class WebCsvController {

	private Map<BigInteger, No> arvore;
	private ResourceLoader resourceLoader;

	WebCsvController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        Resource resource = resourceLoader.getResource("classpath:csv/arvore.csv");
		this.arvore = new HashMap<BigInteger, No>();
		Reader reader;
		try {
			reader = new InputStreamReader(resource.getInputStream());
			CSVParser parser = new CSVParserBuilder()
			    .withSeparator(',')
			    .build();
			CSVReader csvReader = new CSVReaderBuilder(reader)
			    .withSkipLines(1)
			    .withCSVParser(parser)
			    .build();
			String[] line;
		    while ((line = csvReader.readNext()) != null) {
		    	arvore.put(new BigInteger(line[0]), new No(line[1], line[2]));
		    }
		    csvReader.close();
		    reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/")
	String showArvore() {
		String res = "http://localhost:8080/";
		res += "   ou   http://localhost:8080/mensagem";
		res += "   ou   http://localhost:8080/mensagem?conteudo=\"Caros amigos ...\"";
		return res;
	}

	@RequestMapping("/mensagem")
    public String responder(@RequestParam(value="conteudo", defaultValue="Alo") String conteudo) {
		// percorre a arvore
		No no;
		BigInteger id = BigInteger.ZERO;
		while(true) {
			no = arvore.get(id);
			// para compreender certos detalhes, é preciso conhecer como arvore.csv é gerado.
			if(no != null) {
			    if(!no.getPalavra().toLowerCase().contains("null")) { 
				    // não é folha
				    if(conteudo.toLowerCase().contains(no.getPalavra().toLowerCase().trim())) {
					    // mensagem contem a palavra do nó, vai para a esquerda
					    id = id.multiply(new BigInteger("2")).add(BigInteger.ONE);
				    } else {
					    id = id.multiply(new BigInteger("2")).add(BigInteger.ONE).add(BigInteger.ONE);
				    }
			    } else {
				    break;
			    }
			 } else {
			    break;
			 }
		}
		return no.getResposta();
    }
}
