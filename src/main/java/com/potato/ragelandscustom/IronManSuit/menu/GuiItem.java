package com.potato.ragelandscustom.IronManSuit.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GuiItem<A, B> {

    @Getter
    private final A a;
    @Getter
    private final B b;

}