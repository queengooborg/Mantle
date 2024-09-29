package slimeknights.mantle.data.predicate.damage;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import slimeknights.mantle.data.loadable.Loadables;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.PredicateRegistry;
import slimeknights.mantle.data.predicate.TagPredicateRegistry;
import slimeknights.mantle.data.registry.GenericLoaderRegistry.IGenericLoader;

import java.util.List;
import java.util.function.Predicate;

import static slimeknights.mantle.data.registry.GenericLoaderRegistry.SingletonLoader.singleton;

/**
 * Predicate testing for damage sources
 */
public interface DamageSourcePredicate extends IJsonPredicate<DamageSource> {
  /** Predicate that matches all sources */
  DamageSourcePredicate ANY = simple(source -> true);
  /** Loader for item predicates */
  PredicateRegistry<DamageSource> LOADER = new TagPredicateRegistry<>("Damage Source Predicate", ANY, Loadables.DAMAGE_TYPE_TAG, (tag, source) -> source.is(tag));

  /** Damage that protection works against */
  DamageSourcePredicate CAN_PROTECT = simple(source -> !source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS) && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY));

  /* Vanilla getters */
  DamageSourcePredicate PROJECTILE = simple(source -> source.is(DamageTypeTags.IS_PROJECTILE));
  DamageSourcePredicate EXPLOSION = simple(source -> source.is(DamageTypeTags.IS_EXPLOSION));
  DamageSourcePredicate BYPASS_ARMOR = simple(source -> source.is(DamageTypeTags.BYPASSES_ARMOR));
  DamageSourcePredicate DAMAGE_HELMET = simple(source -> source.is(DamageTypeTags.DAMAGES_HELMET));
  DamageSourcePredicate BYPASS_INVULNERABLE = simple(source -> source.is(DamageTypeTags.BYPASSES_INVULNERABILITY));
  DamageSourcePredicate BYPASS_ENCHANTMENTS = simple(source -> source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS));
  DamageSourcePredicate FIRE = simple(source -> source.is(DamageTypeTags.IS_FIRE));
  DamageSourcePredicate FALL = simple(source -> source.is(DamageTypeTags.IS_FALL));

  @Override
  default IJsonPredicate<DamageSource> inverted() {
    return LOADER.invert(this);
  }

  /** Creates a simple predicate with no parameters */
  static DamageSourcePredicate simple(Predicate<DamageSource> predicate) {
    return singleton(loader -> new DamageSourcePredicate() {
      @Override
      public boolean matches(DamageSource source) {
        return predicate.test(source);
      }

      @Override
      public IGenericLoader<? extends DamageSourcePredicate> getLoader() {
        return loader;
      }
    });
  }


  /* Helper methods */

  /** Creates an and predicate */
  @SafeVarargs
  static IJsonPredicate<DamageSource> and(IJsonPredicate<DamageSource>... predicates) {
    return LOADER.and(List.of(predicates));
  }

  /** Creates an or predicate */
  @SafeVarargs
  static IJsonPredicate<DamageSource> or(IJsonPredicate<DamageSource>... predicates) {
    return LOADER.or(List.of(predicates));
  }
}
