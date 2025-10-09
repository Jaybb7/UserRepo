package com.eCommerce.UserModule.Controller;

import com.eCommerce.UserModule.DTO.DeliveryDriverDTO;
import com.eCommerce.UserModule.Enums.OrderStatus;
import com.eCommerce.UserModule.Service.AdminService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private static final Logger logger = Logger.getLogger(AdminController.class.getName());

    @PostMapping("/modifyOrder")
    public ResponseEntity<String> modifyOrder(
            @RequestParam Long orderId,
            @RequestParam OrderStatus orderStatus,
            @RequestParam String comment){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        logger.info("User " + userId + " requested to modify orderId: " + orderId +
                " with status: " + orderStatus + " and comment: '" + comment + "'");

        try {
            boolean flag = adminService.modifyOrder(orderId, orderStatus, comment);

            if (flag) {
                logger.info("User " + userId + " successfully modified orderId: " + orderId);
                return ResponseEntity.ok("Order modification successful for orderId: " + orderId);
            } else {
                logger.warning("User " + userId + " failed to modify orderId: " + orderId);
                return ResponseEntity.internalServerError()
                        .body("Order modification failed for orderId: " + orderId);
            }
        } catch (Exception e) {
            logger.severe("Error while user " + userId + " was modifying orderId: " + orderId +
                    " - " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("An error occurred while modifying the order: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/findBestDrivers")
    public ResponseEntity<List<DeliveryDriverDTO>> findBestDrivers(){
        List<DeliveryDriverDTO> list = adminService.findBestDrivers();
        return ResponseEntity.ok(list);
    }

}
