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
package mage.cards.r;

import java.util.UUID;
import mage.MageInt;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.common.delayed.AtTheBeginOfNextEndStepDelayedTriggeredAbility;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.costs.mana.GenericManaCost;
import mage.abilities.effects.Effect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.ReturnToBattlefieldUnderOwnerControlTargetEffect;
import mage.abilities.keyword.TrampleAbility;
import mage.abilities.keyword.VigilanceAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.SuperType;
import mage.constants.Zone;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.permanent.AnotherPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetCreaturePermanent;
import mage.target.targetpointer.FixedTarget;

/**
 *
 * @author LevelX2
 */
public class RoonOfTheHiddenRealm extends CardImpl {

    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("another target creature");

    static {
        filter.add(new AnotherPredicate());
    }

    public RoonOfTheHiddenRealm(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{2}{G}{W}{U}");
        addSuperType(SuperType.LEGENDARY);
        this.subtype.add("Rhino");
        this.subtype.add("Soldier");

        this.power = new MageInt(4);
        this.toughness = new MageInt(4);

        // Vigilance
        this.addAbility(VigilanceAbility.getInstance());
        // Trample
        this.addAbility(TrampleAbility.getInstance());
        // {2}, {tap}: Exile another target creature. Return that card to the battlefield under its owner's control at the beginning of the next end step.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new RoonOfTheHiddenRealmEffect(), new GenericManaCost(2));
        ability.addTarget(new TargetCreaturePermanent(filter));
        ability.addCost(new TapSourceCost());
        this.addAbility(ability);

    }

    public RoonOfTheHiddenRealm(final RoonOfTheHiddenRealm card) {
        super(card);
    }

    @Override
    public RoonOfTheHiddenRealm copy() {
        return new RoonOfTheHiddenRealm(this);
    }
}

class RoonOfTheHiddenRealmEffect extends OneShotEffect {

    public RoonOfTheHiddenRealmEffect() {
        super(Outcome.Benefit);
        this.staticText = "Exile another target creature. Return that card to the battlefield under its owner's control at the beginning of the next end step";
    }

    public RoonOfTheHiddenRealmEffect(final RoonOfTheHiddenRealmEffect effect) {
        super(effect);
    }

    @Override
    public RoonOfTheHiddenRealmEffect copy() {
        return new RoonOfTheHiddenRealmEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        MageObject sourceObject = game.getObject(source.getSourceId());
        if (controller != null && sourceObject != null) {
            if (getTargetPointer().getFirst(game, source) != null) {
                Permanent permanent = game.getPermanent(getTargetPointer().getFirst(game, source));
                if (permanent != null) {
                    int zcc = permanent.getZoneChangeCounter(game);
                    if (controller.moveCards(permanent, Zone.EXILED, source, game)) {
                        Effect effect = new ReturnToBattlefieldUnderOwnerControlTargetEffect();
                        effect.setTargetPointer(new FixedTarget(permanent.getId(), zcc + 1));
                        AtTheBeginOfNextEndStepDelayedTriggeredAbility delayedAbility
                                = new AtTheBeginOfNextEndStepDelayedTriggeredAbility(effect);
                        game.addDelayedTriggeredAbility(delayedAbility, source);
                    }
                }
            }
            return true;
        }
        return false;
    }
}
