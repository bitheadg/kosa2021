package aa.spring;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BootRestController {

	@RequestMapping("/")
	public String index() {
		return "Spring BootRestController!";
	}
	
    @GetMapping("/array")
    public Collection<String> sayHello() {
        return IntStream.range(0, 10)
          .mapToObj(i -> "incremental test " + i)
          .collect(Collectors.toList());
    }
    
    /*
    @PostMapping("/user")
    public User create(@RequestBody User user) {
        return null;
    }
    */

}