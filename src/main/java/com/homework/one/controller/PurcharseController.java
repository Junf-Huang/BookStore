package com.homework.one.controller;

import com.homework.one.bean.Book;
import com.homework.one.bean.Order;
import com.homework.one.bean.User;
import com.homework.one.responsitory.BookRepository;
import com.homework.one.responsitory.OrderRepository;
import com.homework.one.responsitory.UserRepository;
import com.homework.one.service.UserService;
import oracle.jrockit.jfr.StringConstantPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class PurcharseController {

    private final static Logger logger = LoggerFactory.getLogger(PurcharseController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    UserService userservice;

    @GetMapping(value = "/purchaseList")
    public String getBookList(@RequestParam(defaultValue = "0")int page, Model model) {
        String userId = userservice.getCurrentUser().getStuID();
        logger.info("user={}", userId);

        //find book by id
        if(userId.equals("admin"))
            model.addAttribute("data", orderRepository.findAllByBalance(true,PageRequest.of(page,3, Sort.Direction.DESC,"id")));
        else
            model.addAttribute("data", orderRepository.findOrderByUserIdAndAndBalance(userId,true,PageRequest.of(page,3, Sort.Direction.DESC,"id")));
        model.addAttribute("currentPage",page);

        return "purchaseList";
    }

    @GetMapping(value = "/purchaseBook")
    public String purchaseBook(@RequestParam Integer bookId, @RequestParam int number, @RequestParam(defaultValue = "false") Boolean balance) throws Exception  {

        //得到登录用户的信息
        /*
        Get the username of the logged in user: getPrincipal()
        Get the password of the authenticated user: getCredentials()
        Get the assigned roles of the authenticated user: getAuthorities()
        Get further details of the authenticated user: getDetails()
        */
        String userId = userservice.getCurrentUser().getStuID();
        Book book = bookRepository.findById(bookId).get();
        book.setStockQuantity(book.getStockQuantity()-number);
        Order order = new Order();
        order.setUserId(userId);
        order.setBookId(bookId.toString());
        order.setBookName(book.getBookName());
        order.setNumber(number);
        order.setBalance(balance);
        order.setPrice(book.getPrice()*order.getNumber());

        logger.info("Order={}", order.toString());
        orderRepository.save(order);
        bookRepository.save(book);
        if (order.isBalance())
            return "redirect:/purchaseList";
        else
            return "redirect:/cartList";
    }

    @GetMapping(value = "/cartList")
    public String cartList(@RequestParam(defaultValue = "0")int page, Model model) {
        String userId = userservice.getCurrentUser().getStuID();
        if(userId.equals("admin"))
            model.addAttribute("data", orderRepository.findAllByBalance(false,PageRequest.of(page,3, Sort.Direction.DESC,"id")));
        else
            model.addAttribute("data", orderRepository.findOrderByUserIdAndAndBalance(userId,false,PageRequest.of(page,3, Sort.Direction.DESC,"id")));
        model.addAttribute("currentPage",page);
        return "cartList";

    }

    @GetMapping(value = "/settleAccounts")
    public String settleAccounts(Integer orderId) {
        Order order = orderRepository.findOrderById(orderId).get();
        order.setBalance(true);
        orderRepository.save(order);
        return "redirect:/purchaseList";
    }

    @GetMapping(value = "/deleteOrder")
    public String returnBook(Integer orderId) {

        logger.info("OrderId={}", orderId);
        orderRepository.deleteById(orderId);
        return "redirect:/purchaseList";
    }

}
