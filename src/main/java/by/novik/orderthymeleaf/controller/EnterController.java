package by.novik.orderthymeleaf.controller;


import by.novik.orderthymeleaf.model.Good;
import by.novik.orderthymeleaf.model.Order;

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
@RequestMapping("enter")

@Slf4j
@SessionAttributes("orders")

public class EnterController {
    private final List<Good> shopGoods = new ArrayList<>(Arrays.asList(new Good(1, "Apple", 2),
            new Good(2, "Orange", 3), new Good(3, "Pineapple", 4)));
    private final List<Good> orderGoods = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    private int newId = 0;
    private int orderId = 0;
    private int updateId=0;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("shopGoods", shopGoods);
        model.addAttribute("orderGoods", orderGoods);

        return "enter";
    }

    @GetMapping("add/{id}")
    public String add(@PathVariable int id) {
        Good good = shopGoods.stream().filter(g -> g.getId() == id).findAny().orElseThrow();
        newId++;
        Good addGood = new Good(newId, good.getName(), good.getPrice());
        orderGoods.add(addGood);
        return "redirect:/enter";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable int id) {
        Good good = orderGoods.stream().filter(g -> g.getId() == id).findAny().orElseThrow();
        orderGoods.remove(good);
        return "redirect:/enter";
    }

    @PostMapping
    public String create(Model model, @Valid Order order, Errors errors, SessionStatus status) {
        if (errors.hasErrors()) {
            log.info("order is incorrect: {}", order);
            model.addAttribute("shopGoods", shopGoods);
            model.addAttribute("order", orderGoods);
            return "enter";
        }
        log.info("order is correct: {}", order);
        int i;
        if (updateId>0) {
            i=updateId;
            updateId=0;
        } else {
        orderId++;
        i=orderId;
        }
        newId=0;
        String g=orderGoods.toString();
        Order order1 = new Order(i, g, order.getClientName(), order.getAddress());

        orders.add(order1);
        orderGoods.clear();

        model.addAttribute("shopGoods", shopGoods);
        model.addAttribute("orders", orders);
        status.setComplete();
        return "enter/order";
    }
    @GetMapping("order/delete_order/{id}")
    public String deleteGlobal(@PathVariable int id, Model model) {

        Order order = orders.stream().filter(g -> g.getClientId() == id).findAny().orElseThrow();
        orders.remove(order);
        model.addAttribute("orders", orders);
        return "enter/order";
    }
    @GetMapping("order/update/{id}")
    public String update(@PathVariable int id, Model model) {

        Order order = orders.stream().filter(g -> g.getClientId() == id).findAny().orElseThrow();
        updateId=order.getClientId();
        orders.remove(order);
        model.addAttribute("orders", orders);
        model.addAttribute("shopGoods", shopGoods);
        model.addAttribute("orderGoods", orderGoods);
        return "enter";
    }



    @ModelAttribute(name = "order1")
    public Order getNewOrder() {
        return new Order();
    }
}
