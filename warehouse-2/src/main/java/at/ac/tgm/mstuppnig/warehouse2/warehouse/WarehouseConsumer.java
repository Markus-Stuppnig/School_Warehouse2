package at.ac.tgm.mstuppnig.warehouse2.warehouse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WarehouseConsumer {
	
    @RequestMapping(value="/consumer")
    public String greeting(Model model) {
    	return "consumer";
    }
    
}