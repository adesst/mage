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
package mage.sets.ravnica;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.costs.common.PayLifeCost;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.DoIfCostPaid;
import mage.abilities.effects.common.GainLifeEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetControlledCreaturePermanent;
import mage.target.common.TargetCreatureOrPlayer;

/**
 *
 * @author fireshoes
 */
public class SavraQueenOfTheGolgari extends CardImpl {

    public SavraQueenOfTheGolgari(UUID ownerId) {
        super(ownerId, 225, "Savra, Queen of the Golgari", Rarity.RARE, new CardType[]{CardType.CREATURE}, "{2}{B}{G}");
        this.expansionSetCode = "RAV";
        this.supertype.add("Legendary");
        this.subtype.add("Elf");
        this.subtype.add("Shaman");
        this.power = new MageInt(2);
        this.toughness = new MageInt(2);

        // Whenever you sacrifice a black creature, you may pay 2 life. If you do, each other player sacrifices a creature.
        this.addAbility(new SavraSacrificeBlackCreatureAbility());

        // Whenever you sacrifice a green creature, you may gain 2 life.
        this.addAbility(new SavraSacrificeGreenCreatureAbility());
    }

    public SavraQueenOfTheGolgari(final SavraQueenOfTheGolgari card) {
        super(card);
    }

    @Override
    public SavraQueenOfTheGolgari copy() {
        return new SavraQueenOfTheGolgari(this);
    }
}

class SavraSacrificeBlackCreatureAbility extends TriggeredAbilityImpl {

    public SavraSacrificeBlackCreatureAbility() {
        super(Zone.BATTLEFIELD, new DoIfCostPaid(new SavraSacrificeEffect(), new PayLifeCost(2)));
        this.addTarget(new TargetCreatureOrPlayer());
        this.setLeavesTheBattlefieldTrigger(true);
    }

    public SavraSacrificeBlackCreatureAbility(final SavraSacrificeBlackCreatureAbility ability) {
        super(ability);
    }

    @Override
    public SavraSacrificeBlackCreatureAbility copy() {
        return new SavraSacrificeBlackCreatureAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.SACRIFICED_PERMANENT;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        return event.getPlayerId().equals(this.getControllerId())
                && game.getLastKnownInformation(event.getTargetId(), Zone.BATTLEFIELD).getCardType().contains(CardType.CREATURE)
                && game.getLastKnownInformation(event.getTargetId(), Zone.BATTLEFIELD).getColor(game).isBlack();
    }

    @Override
    public String getRule() {
        return "Whenever you sacrifice a black creature, " + super.getRule();
    }
}

class SavraSacrificeEffect extends OneShotEffect {

    public SavraSacrificeEffect() {
        super(Outcome.Sacrifice);
        this.staticText = "each other player sacrifices a creature";
    }

    public SavraSacrificeEffect(final SavraSacrificeEffect effect) {
        super(effect);
    }

    @Override
    public SavraSacrificeEffect copy() {
        return new SavraSacrificeEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        List<UUID> perms = new ArrayList<>();
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            for (UUID playerId : controller.getInRange()) {
                Player player = game.getPlayer(playerId);
                if (player != null && !playerId.equals(source.getControllerId())) {
                    TargetControlledCreaturePermanent target = new TargetControlledCreaturePermanent();
                    target.setNotTarget(true);
                    if (target.canChoose(player.getId(), game)) {
                        player.chooseTarget(Outcome.Sacrifice, target, source, game);
                        perms.addAll(target.getTargets());
                    }
                }
            }
            for (UUID permID : perms) {
                Permanent permanent = game.getPermanent(permID);
                if (permanent != null) {
                    permanent.sacrifice(source.getSourceId(), game);
                }
            }
            return true;
        }
        return false;
    }
}

class SavraSacrificeGreenCreatureAbility extends TriggeredAbilityImpl {

    public SavraSacrificeGreenCreatureAbility() {
        super(Zone.BATTLEFIELD, new GainLifeEffect(2));
    }

    public SavraSacrificeGreenCreatureAbility(final SavraSacrificeGreenCreatureAbility ability) {
        super(ability);
    }

    @Override
    public SavraSacrificeGreenCreatureAbility copy() {
        return new SavraSacrificeGreenCreatureAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.SACRIFICED_PERMANENT;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        return event.getPlayerId().equals(this.getControllerId())
                && game.getLastKnownInformation(event.getTargetId(), Zone.BATTLEFIELD).getCardType().contains(CardType.CREATURE)
                && game.getLastKnownInformation(event.getTargetId(), Zone.BATTLEFIELD).getColor(game).isGreen();
    }

    @Override
    public String getRule() {
        return "Whenever you sacrifice a green creature, " + super.getRule();
    }
}
