package com.intouch.aligooligo.Target.Controller;

import com.intouch.aligooligo.Target.Controller.Dto.TargetDTO;
import com.intouch.aligooligo.Target.Service.TargetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping(value="/result")
@RestController
@CrossOrigin
public class ResultController {

    private final TargetService targetService;
    
    @GetMapping
    public ResponseEntity<TargetDTO> resultTargetPage(@RequestParam Integer id){

        TargetDTO targetDTO = targetService.resultTargetPage(id);
        if(targetDTO ==null)
            return ResponseEntity.internalServerError().build();
        return ResponseEntity.ok().body(targetDTO);

    }
}
