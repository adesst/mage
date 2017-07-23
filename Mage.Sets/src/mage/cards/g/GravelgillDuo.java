/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.g;

import java.util.UUID;
import mage.MageInt;
import mage.ObjectColor;
import mage.abilities.common.SpellCastControllerTriggeredAbility;
import mage.abilities.effects.common.continuous.BoostSourceEffect;
import mage.abilities.effects.common.continuous.GainAbilitySourceEffect;
import mage.abilities.keyword.FearAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.filter.FilterSpell;
import mage.filter.predicate.mageobject.ColorPredicate;

/**
 *
 * @author North
 */
public class GravelgillDuo extends CardImpl {

    private static final FilterSpell blueFilter = new FilterSpell("a blue spell");
    private static final FilterSpell blackFilter = new FilterSpell("a black spell");

    static {
        blueFilter.add(new ColorPredicate(ObjectColor.BLUE));
        blackFilter.add(new ColorPredicate(ObjectColor.BLACK));
    }

    public GravelgillDuo(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{2}{U/B}");
        this.subtype.add("Merfolk");
        this.subtype.add("Rogue");
        this.subtype.add("Warrior");

        this.power = new MageInt(2);
        this.toughness = new MageInt(1);

        // Whenever you cast a blue spell, Gravelgill Duo gets +1/+1 until end of turn.
        this.addAbility(new SpellCastControllerTriggeredAbility(new BoostSourceEffect(1, 1, Duration.EndOfTurn), blueFilter, false));
        // Whenever you cast a black spell, Gravelgill Duo gains fear until end of turn.
        this.addAbility(new SpellCastControllerTriggeredAbility(new GainAbilitySourceEffect(FearAbility.getInstance(), Duration.EndOfTurn), blackFilter, false));
    }

    public GravelgillDuo(final GravelgillDuo card) {
        super(card);
    }

    @Override
    public GravelgillDuo copy() {
        return new GravelgillDuo(this);
    }
}
