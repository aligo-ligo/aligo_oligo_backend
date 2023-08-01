package com.intouch.aligooligo.Controller;


import com.intouch.aligooligo.Jwt.JwtTokenProvider;
import com.intouch.aligooligo.dto.TargetDTO;
import com.intouch.aligooligo.Service.TargetService;
import com.intouch.aligooligo.dto.TargetlistDTO;
import com.intouch.aligooligo.req.TargetUpdateReq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping(value="/target")
@RestController
@CrossOrigin
public class TargetController {
    private final JwtTokenProvider jwtTokenProvider;
    private final TargetService targetService;

    @GetMapping("/list")
    public ResponseEntity<List<TargetlistDTO>> getTargetList(HttpServletRequest request){
        try{
            String email = checkJwtValidation(request);
            if(email==null)
                return ResponseEntity.status(401).build();
            List<TargetlistDTO> list = targetService.getTargetList(email);
            return ResponseEntity.ok().body(list);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createTarget(HttpServletRequest request, @RequestBody TargetDTO req) {
        try {
            String email = checkJwtValidation(request);
            if(email==null)
                return ResponseEntity.status(401).build();//not auth

            boolean created = targetService.createTarget(email, req);
            if(created)
                return ResponseEntity.ok().build();//ok
            return ResponseEntity.internalServerError().build();//server error
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();//server error
        }
    }

    public String checkJwtValidation(HttpServletRequest request){
        try {
            String token = request.getHeader("Authorization");
            if (token != null) {
                String req_token = token.substring(7);
                if(jwtTokenProvider.validateToken(req_token)) {
                    return jwtTokenProvider.getAuthentication(req_token).getName();
                }
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<TargetDTO> detailTarget(HttpServletRequest request, @RequestParam Integer id){
        try{
            String email = checkJwtValidation(request);
            if(email==null)
                return ResponseEntity.status(401).build();
            TargetDTO targetDTO = targetService.getDetailTarget(id);
            if(targetDTO==null)
                return ResponseEntity.internalServerError().build();
            return ResponseEntity.ok().body(targetDTO);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<TargetDTO> updateTarget(HttpServletRequest request, @RequestBody TargetUpdateReq req){
        try{
            String email = checkJwtValidation(request);
            TargetDTO targetDTO = targetService.updateTarget(req);
            if(email==null)
                return ResponseEntity.status(401).build();
            if(req==null){
                return ResponseEntity.badRequest().build();
            }
            if(targetDTO==null){
                return ResponseEntity.internalServerError().build();
            }
            return ResponseEntity.ok().body(targetDTO);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/vote")
    public ResponseEntity<HttpStatus> voteTarget(@RequestParam(value = "id") Integer id, @RequestParam(value = "success") boolean success){
        try{
            boolean voted = targetService.voteTarget(id, success);
            if(voted)
                return ResponseEntity.ok().build();//ok
            return ResponseEntity.internalServerError().build();//server error
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();//server error
        }
    }
}
