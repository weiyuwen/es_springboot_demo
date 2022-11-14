package com.chixing.controller;

import com.chixing.entity.User;
import com.chixing.service.EsUSerService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import javax.websocket.server.PathParam;
import java.util.*;

@RestController
public class EsUserController {
    private final EsUSerService userService;
    private final ElasticsearchRestTemplate restTemplate;

    public EsUserController(EsUSerService userService, ElasticsearchRestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/saveUser")
    public String saveUser(){
        Random random=new Random();
        List<User> users=new ArrayList<>();
        User user1=new User(1,21,"上海的小李","上周的天气不错，海边风大");
        User user2=new User(2,20,"上海的小王","昨天的天气不错，海边风大");
        User user3=new User(3,21,"浙江的老王","昨天的天气不错，海边风大");
        User user4=new User(4,21,"苏州的小赵","上周的天气不错，海边风大");
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        Iterable<User> users1 = userService.saveAll(users);
        return "save successfully";
    }
    //根据id查找
    @GetMapping("/getById")
    public Optional<User> getById(@PathParam("id") Integer id){
        Optional<User> user=userService.findById(id);
        System.out.println("select by id: "+user);
        return user;
    }
    //分页查找
    @GetMapping("/getByPage")
    public Page<User> getByPage(){
        Pageable page= PageRequest.of(0,2, Sort.Direction.ASC,"id");
        Page<User> allUser=userService.findAll(page);
        System.out.println("user: "+allUser);
        return allUser;
    }
    //根据名字查找
    @GetMapping("/seleteByName")
    public List<User> seleteByName(@PathParam("name") String name){
        System.out.println("selete by name ok");
        return userService.findByName(name);
    }
    //查询高亮显示
    @GetMapping("/hightLight")
    public List<User> hightLight(@PathParam("kw") String kw){
        //1.需要查询的字段
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder queryBuilder =QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("info",kw))
                .should(QueryBuilders.matchQuery("name",kw));
        //2.构建高亮查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withHighlightFields(new HighlightBuilder.Field("info"),new HighlightBuilder.Field("name"))
                .withHighlightBuilder(new HighlightBuilder().
                        preTags("<span style='color:red'>").
                        postTags("</span>")).build();
        //3.查询
        SearchHits<User> search = restTemplate.search(searchQuery,User.class);
        //4.得到查询返回的内容
        List<SearchHit<User>> searchHit = search.getSearchHits();
        //5.设置一个要返回的实体类集合
        List<User> userslist = new ArrayList<>();
        for (SearchHit<User> searchHit1:searchHit
             ) {
            //6.高亮内容
            Map<String, List<String>> highlightFields = searchHit1.getHighlightFields();
            //7.将高亮的内容填充到content中
            String name=highlightFields.get("name")==null?searchHit1.getContent().getName()
                    :highlightFields.get("name").get(0);
            String info=highlightFields.get("info")==null?searchHit1.getContent().getInfo()
                    :highlightFields.get("info").get(0);
            searchHit1.getContent().setName(name);
            searchHit1.getContent().setInfo(info);
            //8.放到实体类中
            userslist.add(searchHit1.getContent());
        }
        //9.返回实体类
        System.out.println("find hight light is ok");
        return userslist;
    }

}
