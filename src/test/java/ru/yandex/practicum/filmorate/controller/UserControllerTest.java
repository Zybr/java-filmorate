package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.yandex.practicum.filmorate.factory.Factory;
import ru.yandex.practicum.filmorate.factory.UserFactory;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest extends ModelControllerTest<User> {
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private UserService userService;

    /**
     * @see UserController#getOne(Long)
     */
    @Test
    public void shouldGetUserById() throws Exception {
        User existedUser = createModel();
        User fetchedUser = doGetOneRequest(existedUser.getId());
        this.assertEqualModels(existedUser, fetchedUser);
    }

    /**
     * @see UserController#addFriend(Long, Long)
     */
    @Test
    public void shouldAddFriend() throws Exception {
        User userA = createModel();
        User userB = createModel();

        doAddFriendRequest(
                userA.getId(),
                userB.getId()
        );

        Assertions.assertTrue(
                userService.isFriend(
                        refreshUser(userA),
                        refreshUser(userB)
                )
        );
    }

    /**
     * @see UserController#removeFriend(Long, Long)
     */
    @Test
    public void shouldRemoveFriend() throws Exception {
        User userA = createModel();
        User userB = createModel();

        doAddFriendRequest(
                userA.getId(),
                userB.getId()
        );
        Assertions.assertTrue(
                userService.isFriend(
                        refreshUser(userA),
                        refreshUser(userB)
                )
        );

        doRemoveFriendRequest(
                userA.getId(),
                userB.getId()
        );
        Assertions.assertFalse(
                userService.isFriend(
                        refreshUser(userA),
                        refreshUser(userB)
                )
        );
    }

    /**
     * @see UserController#getFriends(Long)
     */
    @Test
    public void shouldGetFriends() throws Exception {
        User user = createModel();
        List<Long> addedFriends = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            User friend = createModel();
            doAddFriendRequest(
                    user.getId(),
                    friend.getId()
            );
            addedFriends.add(
                    friend.getId()
            );
        }

        List<Long> fetchedFriends = doGetFiendsRequest(user.getId())
                .stream()
                .map(User::getId)
                .sorted()
                .toList();

        Assertions.assertEquals(
                addedFriends,
                fetchedFriends
        );
    }

    /**
     * @see UserController#getCommonFriends(Long, Long)
     */
    @Test
    public void shouldGetCommonFriends() throws Exception {
        User userA = createModel();
        User userB = createModel();
        User userC = createModel();
        doAddFriendRequest(
                userA.getId(),
                userC.getId()
        );
        doAddFriendRequest(
                userB.getId(),
                userC.getId()
        );
        List<Long> commonFriends = new ArrayList<>();
        commonFriends.add(userC.getId());

        List<Long> fetchedCommonFriends = doGetCommonFriendsRequest(
                userA.getId(),
                userB.getId()
        )
                .stream()
                .map(User::getId)
                .toList();

        Assertions.assertEquals(
                fetchedCommonFriends,
                commonFriends
        );
    }

    @Test
    public void shouldHandleNotExisted() throws Exception {
        Long notExistedId = Math.round(Math.pow(10, 10));

        doGetOneRequest(
                notExistedId,
                status().isNotFound()
        );
        doAddFriendRequest(
                notExistedId,
                notExistedId,
                status().isNotFound()
        );
        doRemoveFriendRequest(
                notExistedId,
                notExistedId,
                status().isNotFound()
        );
        doGetFiendsRequest(
                notExistedId,
                status().isNotFound()
        );
        doGetCommonFriendsRequest(
                notExistedId,
                notExistedId,
                status().isNotFound()
        );
    }

    @Override
    protected String getRootPath() {
        return "/users";
    }

    @Override
    protected Factory<User> getModelFactory() {
        return new UserFactory();
    }

    @Override
    protected User attributesToModel(LinkedHashMap<String, Object> attributes) throws ParseException {
        return User.builder()
                .id(Long.valueOf(attributes.get("id").toString()))
                .email(attributes.get("email").toString())
                .login(attributes.get("login").toString())
                .name(attributes.get("name").toString())
                .birthday(parseDate(
                        attributes.get("birthday").toString()
                ))
                .build();
    }

    protected ArrayList<User> makeInvalidModels() throws ParseException {
        ArrayList<User> models = new ArrayList<>();
        models.add(
                makeModel().toBuilder()
                        .email("")
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .email("invalid-email")
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .email(null)
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .login("")
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .login("with space")
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .login(null)
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .birthday(parseDate("2030-01-01"))
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .birthday(null)
                        .build()
        );

        return models;
    }

    private User doGetOneRequest(
            Long id
    ) throws Exception {
        return doGetOneRequest(
                id,
                status().isOk()
        );
    }

    private User doGetOneRequest(
            Long id,
            ResultMatcher matcher
    ) throws Exception {
        MockHttpServletResponse response = mvc.perform(
                        get(
                                String.format(
                                        "%s/%d",
                                        getRootPath(),
                                        id
                                )
                        )
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(matcher)
                .andReturn()
                .getResponse();

        return response.getStatus() < 300
                ? jsonToModel(response.getContentAsString())
                : null;
    }

    private void doAddFriendRequest(
            Long userId,
            Long friendId
    ) throws Exception {
        doAddFriendRequest(
                userId,
                friendId,
                status().isOk()
        );
    }

    private void doAddFriendRequest(
            Long userId,
            Long friendId,
            ResultMatcher matcher
    ) throws Exception {
        mvc.perform(
                        put(
                                String.format(
                                        "%s/%d/friends/%d",
                                        getRootPath(),
                                        userId,
                                        friendId
                                )
                        )
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(matcher);
    }

    private void doRemoveFriendRequest(
            Long userId,
            Long friendId
    ) throws Exception {
        doRemoveFriendRequest(
                userId,
                friendId,
                status().isOk()
        );
    }

    private void doRemoveFriendRequest(
            Long userId,
            Long friendId,
            ResultMatcher matcher
    ) throws Exception {
        mvc.perform(
                        delete(
                                String.format(
                                        "%s/%d/friends/%d",
                                        getRootPath(),
                                        userId,
                                        friendId
                                )
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(matcher)
                .andReturn()
                .getResponse();
    }

    private List<User> doGetFiendsRequest(Long id) throws Exception {
        return doGetFiendsRequest(
                id,
                status().isOk()
        );
    }

    private List<User> doGetFiendsRequest(
            Long id,
            ResultMatcher matcher
    ) throws Exception {
        MockHttpServletResponse response = mvc.perform(
                        get(
                                String.format(
                                        "%s/%d/friends",
                                        getRootPath(),
                                        id
                                )
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(matcher)
                .andReturn()
                .getResponse();

        String responseBody = response
                .getContentAsString();

        return response.getStatus() <= 300 ? jsonToModels(responseBody) : null;
    }

    private List<User> doGetCommonFriendsRequest(
            Long userIdA,
            Long userIdB
    ) throws Exception {
        return doGetCommonFriendsRequest(
                userIdA,
                userIdB,
                status().isOk()
        );
    }

    private List<User> doGetCommonFriendsRequest(
            Long userIdA,
            Long userIdB,
            ResultMatcher matcher
    ) throws Exception {
        MockHttpServletResponse response = mvc.perform(
                        get(
                                String.format(
                                        "%s/%d/friends/common/%d",
                                        getRootPath(),
                                        userIdA,
                                        userIdB
                                )
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(matcher)
                .andReturn()
                .getResponse();

        return response.getStatus() < 300
                ? jsonToModels(response.getContentAsString())
                .stream()
                .toList()
                : null;
    }

    private User refreshUser(User user) {
        return this.userStorage.findOne(user.getId());
    }
}
