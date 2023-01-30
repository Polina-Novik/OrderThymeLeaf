package by.novik.orderthymeleaf.controller;


import by.novik.thymeleaforder.model.Good;
import by.novik.thymeleaforder.model.Order;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("update")
@Slf4j
@SessionAttributes("order1")

public class UpdateController {
    private final List<Good> shopGoods = new ArrayList<>(Arrays.asList(new Good(1, "Apple", 2),
            new Good(2, "Orange", 3), new Good(3, "Pineapple", 4)));
    private List<Good> orderGoods=new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private int newId = 0;


    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("shopGoods", shopGoods);
        model.addAttribute("orderGoods", orderGoods);
        model.addAttribute("orders", orders);
        return "form";
    }

    @GetMapping("add/{id}")
    public String add(@PathVariable int id) {
        Good good = shopGoods.stream().filter(g -> g.getId() == id).findAny().orElseThrow();
        newId++;
        Good addGood = new Good(newId, good.getName(), good.getPrice());
        orderGoods.add(addGood);
        return "redirect:/update";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable int id) {
        Good good = orderGoods.stream().filter(g -> g.getId() == id).findAny().orElseThrow();
        orderGoods.remove(good);
        return "redirect:/update";
    }

    @PostMapping
    public String update(Model model, @Valid Order order, Errors errors, SessionStatus status,int id) {
        if (errors.hasErrors()) {
            log.info("order is incorrect: {}", order);
            model.addAttribute("shopGoods", shopGoods);
            model.addAttribute("order", orderGoods);
            return "form";
        }
        log.info("order is correct: {}", order);
        String g=orderGoods.toString();
        Order order1 = new Order(id, g, order.getClientName(), order.getAddress());
        orders.add(order1);
        orderGoods.clear();
        model.addAttribute("shopGoods", shopGoods);
        model.addAttribute("orders", orders);
        status.setComplete();
        return "order";
    }



    @ModelAttribute(name = "order1")
    public Order getNewOrder() {
        return new Order();
    }
}
