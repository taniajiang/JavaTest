package com.example.tania.controller;

import com.example.tania.module.SearchData;
import com.example.tania.module.Statement;
import com.example.tania.module.User;
import com.example.tania.service.StatementService;
import com.example.tania.validation.SearchDataValidation;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.ListUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    @Resource
    private StatementService statementService;
    @Resource
    private SearchDataValidation searchDataValidation;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView loginPage(final Model model, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(final Model model, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

   /* @RequestMapping(value = "/dologin", method = RequestMethod.POST)
    public ModelAndView doLogin(User user, ModelAndView mv, HttpServletRequest request, Model model){
        if (user!=null){
            String userName = user.getUsername();
            if("testadmin".equals(user.getUsername())){
                request.getSession().setAttribute("login",userName);
                request.getSession().setAttribute("login_role","admin");
            }else if ("testUser".equals(user.getUsername())){
                request.getSession().setAttribute("login",userName);
                request.getSession().setAttribute("login_role","test");
            }
            model.addAttribute("loginName", userName);
            List<Statement> statements = statementService.getAllStatement();
            model.addAttribute("statements", statements);
            mv.setViewName("main");
        }else{
            model.addAttribute("errorMessage", "Invalid username or password.");
            mv.setViewName("login");
        }

        return mv;
    }*/

    @RequestMapping(value = "/search")
    public ModelAndView search(SearchData searchData, final Model model, BindingResult bindingResult){
        ModelAndView mav = new ModelAndView();
        buildAttribute(searchData, model);

        List<Statement> statements = new ArrayList<>();
        if (searchData != null && StringUtils.isNotBlank(searchData.getAccountId())) {
            try{
                validRoleAuth(searchData);
            }catch (HttpClientErrorException e){
                model.addAttribute("errorMessage", e.getMessage());
                return getModelAndView(model, mav, statements);
            }

            searchDataValidation.validate(searchData, bindingResult);
            if (bindingResult.hasErrors()) {
                List<ObjectError> errors = bindingResult.getAllErrors();
                model.addAttribute("errorMessage", errors.get(0).getCode());
                return getModelAndView(model, mav, statements);
            }
            statements = statementService.getStatementsById(searchData);
        }else{
            statements = statementService.getAllStatement();
        }
        model.addAttribute("statements", statements);
        mav.setViewName("main");
        return mav;
    }

    private ModelAndView getModelAndView(Model model, ModelAndView mav, List<Statement> statements) {
        model.addAttribute("statements", statements);
        mav.setViewName("main");
        return mav;
    }

    private void validRoleAuth(SearchData searchData)  throws HttpClientErrorException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userRole = userDetails.getAuthorities().iterator().next().toString();
        if (StringUtils.isNotBlank(userRole) && !ADMIN_ROLE.equals(userRole) && hasOtherCondition(searchData)){
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Current user don't have access for detailed (Date and Amount) search.");
        }
    }

    private void buildAttribute(SearchData searchData, Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("loginName", userDetails.getUsername());
        model.addAttribute("accountId", searchData.getAccountId());
        model.addAttribute("selectDate", searchData.getSelectDate());
        model.addAttribute("fromDate", searchData.getFromDate());
        model.addAttribute("toDate", searchData.getToDate());
        model.addAttribute("inputAmount", searchData.getInputAmount());
        model.addAttribute("fromAmount", searchData.getFromAmount());
        model.addAttribute("toAmount", searchData.getToAmount());
    }

    private boolean hasOtherCondition(final SearchData searchData){
        String selectDate = searchData.getSelectDate();
        String fromDate = searchData.getFromDate();
        String toDate = searchData.getToDate();
        String inputAmount = searchData.getInputAmount();
        String fromAmount = searchData.getFromAmount();
        String toAmount = searchData.getToAmount();

        return StringUtils.isNotBlank(selectDate) || StringUtils.isNotBlank(fromDate)
                || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(inputAmount)
                || StringUtils.isNotBlank(fromAmount) || StringUtils.isNotBlank(toAmount) ;
    }

}
