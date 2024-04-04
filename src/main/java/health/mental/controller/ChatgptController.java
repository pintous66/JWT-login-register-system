package health.mental.controller;

import health.gpt.model.ChatgptRequest;
import health.gpt.model.ChatgptResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/gpt")
public class ChatgptController {


    @Value("${chatgpt.model}")
    private String model;

    @Value("${chatgpt.api.url}")
    private String apiUrl;

    @Value("${chatgpt.api.key}")
    private String apiKey;

    private static RestTemplate restTemplate = new RestTemplate();
    //endpoint in the form of /gpt/ask?query=your_query
    @RequestMapping(value = "/ask",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String ask(@RequestBody String query) {
        ChatgptRequest chatgptRequest = new ChatgptRequest(model, query);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + apiKey);
        ChatgptResponse chatgptResponse = restTemplate.postForObject(apiUrl,
                                                            new HttpEntity<>(chatgptRequest,headers),
                                                            ChatgptResponse.class);
        return chatgptResponse.getChoices().get(0).getMessage().getContent();
    }
}
