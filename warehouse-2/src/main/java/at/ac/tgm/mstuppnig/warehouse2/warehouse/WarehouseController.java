package at.ac.tgm.mstuppnig.warehouse2.warehouse;

import at.ac.tgm.mstuppnig.warehouse2.Warehouse2Application;
import at.ac.tgm.mstuppnig.warehouse2.io.LocalListener;
import at.ac.tgm.mstuppnig.warehouse2.model.WarehouseData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
public class WarehouseController {

    @Autowired
    private WarehouseService service;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/")
    public String warehouseMain() {
    	String mainPage = "Warehouse Application<br/><br/>" +
                          "<a href='http://localhost:8080/warehouse/001/data'>Link to warehouse/001/data</a><br/>" +
                          "<a href='http://localhost:8080/warehouse/001/xml'>Link to warehouse/001/xml</a><br/>" +
                          "<a href='http://localhost:8080/warehouse/001/transfer'>Link to warehouse/001/transfer</a><br/>";
        return mainPage;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value="/warehouse/{inID}/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public WarehouseData warehouseData(@PathVariable String inID ) {
        WarehouseData data = service.getWarehouseData( inID );

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Warehouse2Application.sender.sendMessageToTopic(jsonString);
        return data;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value="/warehouse/main", produces = MediaType.APPLICATION_JSON_VALUE)
    public String warehouseMainData() {

        if(Warehouse2Application.main) {
            Warehouse2Application.sender.sendMessageToTopic("Main: " + LocalDateTime.now().toString());
            return LocalListener.data.toString();
        }
        return null;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value="/warehouse/{inID}/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public WarehouseData warehouseDataXML( @PathVariable String inID ) {
        return service.getWarehouseData( inID );
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/warehouse/{inID}/transfer")
    public String warehouseTransfer( @PathVariable String inID ) {
        return service.getGreetings("Warehouse.Transfer!");
    }

}