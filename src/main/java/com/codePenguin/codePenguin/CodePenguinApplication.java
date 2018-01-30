package com.codePenguin.codePenguin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class CodePenguinApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        System.out.println("Main start");
        SpringApplication.run(CodePenguinApplication.class, args);
        System.out.println("Main end");

    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository,
                                      GameRepository gameRepository,
                                      GamePlayerRepository gamePlayerRepository,
                                      ShipRepository shipRepository,
                                      SalvoRepository salvoRepository,
                                      ScoreRepository scoreRepository)
    {
        System.out.println("initData running");
        return (args) -> {
            System.out.println("CLR running");

            // PLAYERS
            Player player1 = new Player("victorgreco263@gmail.com", "c");
            Player player2 = new Player("victor@gmail.com", "Az26!");
            Player player3 = new Player("aslamasda@gmail.com", "Asdas!|");

            //DATES
            Date date1 = new Date();
            Date date2 = new Date();
                date2= Date.from(date2.toInstant().plusSeconds(3600));
            Date date3 = new Date();
                date3= Date.from(date3.toInstant().plusSeconds(7200));

            //GAMES
            Game game1 = new Game(date1);
            Game game2 = new Game(date2);
            Game game3 = new Game(date3);

            //GAMEPLAYERS
            GamePlayer gamePlayer1 = new GamePlayer(game1, player1);
            GamePlayer gamePlayer2 = new GamePlayer(game1, player2);

            GamePlayer gamePlayer3 = new GamePlayer(game2, player2);
            GamePlayer gamePlayer4 = new GamePlayer(game2, player3);

            GamePlayer gamePlayer5 = new GamePlayer(game3, player1);
            GamePlayer gamePlayer6 = new GamePlayer(game3, player3);

            //SHIPS POSITIONS
            List<String> patrolBoat_Loc = new ArrayList<>(Arrays.asList("A1", "A2"));
            List<String> destroyer_Loc = new ArrayList<>(Arrays.asList("B1", "B2", "B3"));
            List<String> submarine_Loc = new ArrayList<>(Arrays.asList("C1", "C2", "C3"));
            List<String> battleship_Loc = new ArrayList<>(Arrays.asList("D1", "D2", "D3","D4"));
            List<String> carrier_Loc = new ArrayList<>(Arrays.asList("A7", "B7", "C7","D7","E7"));

            //SHIPS GAMEPLAYER 1
            Ship patrolBoat = new Ship("PatrolBoat", patrolBoat_Loc, gamePlayer1);
            Ship destroyer = new Ship("Destroyer", destroyer_Loc, gamePlayer1);
            Ship submarine = new Ship("Submarine", submarine_Loc, gamePlayer1);
            Ship battleship = new Ship("Battleship", battleship_Loc, gamePlayer1);
            Ship carrier = new Ship("Carrier", carrier_Loc, gamePlayer1);
            //SHIPS GAMEPLAYER 2
            Ship patrolBoat2 = new Ship("PatrolBoat", patrolBoat_Loc, gamePlayer2);
            Ship carrier2 = new Ship("Carrier", carrier_Loc, gamePlayer2);


            //SINGLE SHOTS
            SingleShot shot1 = new SingleShot("J1");
            SingleShot shot2 = new SingleShot("B7");
            SingleShot shot3 = new SingleShot("J3");


            //SALVOES POSITIONS
            List<SingleShot> turn1Player1SalvoPos = new ArrayList<>(Arrays.asList(shot1, shot2 , shot3));
            List<SingleShot> turn1Player2SalvoPos = new ArrayList<>(Arrays.asList(shot1,shot2 , shot3));

            List<SingleShot> turn2Player1SalvoPos = new ArrayList<>(Arrays.asList(shot1,shot2 , shot3));
            List<SingleShot> turn2Player2SalvoPos = new ArrayList<>(Arrays.asList(shot1,shot2 , shot3));

            List<SingleShot> turn3Player1SalvoPos = new ArrayList<>(Arrays.asList(shot1,shot2 , shot3));
            List<SingleShot> turn3Player2SalvoPos = new ArrayList<>(Arrays.asList(shot1,shot2 , shot3));

//            int Turn = 1;

            //SALVOES
            Salvo turn1Player1 = new Salvo(1, gamePlayer1, turn1Player1SalvoPos);
            Salvo turn1Player2 = new Salvo(1, gamePlayer2, turn1Player2SalvoPos);

            Salvo turn2Player1 = new Salvo(2, gamePlayer1, turn2Player1SalvoPos);
            Salvo turn2Player2 = new Salvo(2, gamePlayer2, turn2Player2SalvoPos);

            Salvo turn3Player1 = new Salvo(3, gamePlayer1, turn3Player1SalvoPos);
            Salvo turn3Player2 = new Salvo(3, gamePlayer2, turn3Player2SalvoPos);

            //SCORES
            Score scoreGame1Player1 = new Score(0, game1, player1);
            Score scoreGame1Player2 = new Score(2, game1, player2);

            Score scoreGame2Player2 = new Score(2, game2, player2);
            Score scoreGame2Player3 = new Score(0, game2, player3);

            Score scoreGame3Player1 = new Score(1, game3, player1);
            Score scoreGame3Player3 = new Score(1, game3, player3);




            // PLAYER REPOSITORY
            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player3);

            // GAMES REPOSITORY
            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);

            // GAMEPLAYERS REPOSITORY
            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);
            gamePlayerRepository.save(gamePlayer3);
            gamePlayerRepository.save(gamePlayer4);
            gamePlayerRepository.save(gamePlayer5);
            gamePlayerRepository.save(gamePlayer6);

            // SHIPS REPOSITORY
            shipRepository.save(patrolBoat);
            shipRepository.save(destroyer);
            shipRepository.save(submarine);
            shipRepository.save(battleship);
            shipRepository.save(carrier);

            shipRepository.save(patrolBoat2);
            shipRepository.save(carrier2);

            // SALVOES REPOSITORY
            salvoRepository.save(turn1Player1);
            salvoRepository.save(turn1Player2);

            salvoRepository.save(turn2Player1);
            salvoRepository.save(turn2Player2);

            salvoRepository.save(turn3Player1);
            salvoRepository.save(turn3Player2);

            // SCORES REPOSITORY
            scoreRepository.save(scoreGame1Player1);
            scoreRepository.save(scoreGame1Player2);

            scoreRepository.save(scoreGame2Player2);
            scoreRepository.save(scoreGame2Player3);

            scoreRepository.save(scoreGame3Player1);
            scoreRepository.save(scoreGame3Player3);

        };

    }
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
   @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
                Player player = playerRepository.findByUserName(name);
                    if (player != null) {
                        return new User(player.getUserName(), player.getPassword(),
                                AuthorityUtils.createAuthorityList("USER"));
                    }else{
                        throw new UsernameNotFoundException("Unknown user: " + name);
                    }


            }
        };
    }
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/web/games.html").permitAll()
                .antMatchers("/web/games.css").permitAll()
                .antMatchers("/web/games.js").permitAll()
                .antMatchers("/web/resources/**").permitAll()
                .antMatchers("/api/games").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/players").permitAll()
                .antMatchers("/api/game_view").hasAnyAuthority("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/api/login")
                .and()
                .logout()
                .logoutUrl("/api/logout");


        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }


    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
