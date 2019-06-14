package com.revolut.bank;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    @Test
    public void appHasAGreeting() {
        App app = new App();
        assertThat(app.getGreeting()).isNotNull();
    }

}
