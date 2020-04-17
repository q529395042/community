package com.ch.community.controller;

import com.ch.community.mapper.QuestionMapper;
import com.ch.community.mapper.UserMapper;
import com.ch.community.model.QuestionExample;
import com.ch.community.model.UserExample;
import com.ch.community.model.Question;
import com.ch.community.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublicController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/public")
    public String release(){
        return "public";
    }

    @PostMapping("/public")
    public String insertQuestion(@RequestParam(name = "id",required = false) String id,String title, String description, String tag, HttpServletRequest request, Model model){
        System.out.println(tag);
        if (title==null||title.equals("")){
            request.setAttribute("error","标题不能为空");
            return "public";
        }
        if (description==null||description.equals("")){
            request.setAttribute("error","问题补充不能为空");
            return "public";
        }
        if (tag==null||tag.equals("")){
            request.setAttribute("error","标签不能为空");
            return "public";

        }

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        Cookie[] cookies=request.getCookies();
        if(cookies==null){
            request.setAttribute("error","用户未登录");
            return "public";
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){
               String token=cookie.getValue();
                UserExample userExample=new UserExample();
                userExample.createCriteria().andTokenEqualTo(token);
                    User user=userMapper.selectByExample(userExample).get(0);
                if (user==null){
                    request.setAttribute("error","用户未登录");
                    return "public";
                }
                Question question=new Question();

                question.setCreator(user.getId());
                question.setTitle(title);
                question.setDescription(description);
                question.setTag(tag);
                question.setGmtModified(System.currentTimeMillis());
                if (id!=null&&!id.equals("")){
                    question.setId(Integer.parseInt(id));
                    QuestionExample questionExample=new QuestionExample();
                    questionMapper.updateByPrimaryKeySelective(question);
                    System.out.println("更新");
                    return "redirect:frist";
                }
                if (user!=null){
                    System.out.println("插入");
                    question.setCommentCount(0);
                    question.setLikeCount(0);
                    question.setViewCount(0);
                    question.setGmtCreate(System.currentTimeMillis());
                    questionMapper.insert(question);
                    request.setAttribute("success","发布成功");
                    return "redirect:frist";
                }
            }
        }
        return "public";
    }


    @GetMapping("/public/{id}")
    public String modifyQuestion(@PathVariable("id")Integer id, Model model){
        if (id!=null){
            Question question=questionMapper.selectByPrimaryKey(id);
            model.addAttribute("question",question);
            model.addAttribute("title",question.getTitle());
            model.addAttribute("id",question.getId());
            model.addAttribute("description",question.getDescription());
            model.addAttribute("tag",question.getTag());
            return "public";
        }
        return "frist";
    }
}
