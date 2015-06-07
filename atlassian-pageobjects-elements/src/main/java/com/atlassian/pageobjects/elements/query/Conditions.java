package com.atlassian.pageobjects.elements.query;

import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import com.atlassian.pageobjects.elements.timeout.Timeouts;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import org.apache.commons.lang.ArrayUtils;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.atlassian.pageobjects.elements.util.StringConcat.asString;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;

/**
 * Utilities to create miscellaneous {@link TimedCondition}s.
 *
 */
@SuppressWarnings("unchecked")
public final class Conditions
{
    private static final Logger log = LoggerFactory.getLogger(Conditions.class);

    private static final int DEFAULT_TIMEOUT = 100;

    private Conditions()
    {
        throw new AssertionError(Conditions.class.getName() + " should not be instantiated");
    }

    /**
     * Return new timed condition that is a negation of <tt>condition</tt>.
     *
     * @param condition condition to be negated
     * @return negated {@link TimedCondition} instance.
     */
    @Nonnull
    public static TimedQuery<Boolean> not(@Nonnull TimedQuery<Boolean> condition)
    {
        if (condition instanceof Not)
        {
            return asDecorator(condition).wrapped;
        }
        return new Not(condition);
    }

    /**
     * <p>
     * Return new combinable condition that is logical product of <tt>conditions</tt>.
     *
     * <p>
     * The resulting condition will have interval of the first condition
     * in the <tt>conditions</tt> array,
     *
     * @param conditions conditions to conjoin
     * @return product of <tt>conditions</tt>
     * @throws IllegalArgumentException if <tt>conditions</tt> array is <code>null</code> or empty
     *
     * @see TimedCondition#interval()
     */
    @Nonnull
    public static CombinableCondition and(@Nonnull TimedQuery<Boolean>... conditions)
    {
        return new And(conditions);
    }

    /**
     * <p>
     * Return new combinable condition that is logical product of <tt>conditions</tt>.
     *
     * <p>
     * The resulting condition will have interval of the first condition
     * in the <tt>conditions</tt> array,
     *
     * @param conditions conditions to conjoin
     * @return product of <tt>conditions</tt>
     * @throws IllegalArgumentException if <tt>conditions</tt> array is <code>null</code> or empty
     *
     * @see TimedCondition#interval()
     */
    @Nonnull
    public static CombinableCondition and(@Nonnull Iterable<TimedQuery<Boolean>> conditions)
    {
        return and(Iterables.toArray(conditions, TimedQuery.class));
    }

    /**
     * <p>
     * Return new combinable condition that is logical sum of <tt>conditions</tt>.
     *
     * <p>
     * The resulting condition will have interval of the first condition
     * in the <tt>conditions</tt> array,
     *
     * @param conditions conditions to sum
     * @return logical sum of <tt>conditions</tt>
     * @throws IllegalArgumentException if <tt>conditions</tt> array is <code>null</code> or empty
     *
     * @see TimedCondition#interval()
     */
    @Nonnull
    public static CombinableCondition or(@Nonnull TimedQuery<Boolean>... conditions)
    {
        return new Or(conditions);
    }

    /**
     * <p>
     * Return new combinable condition that is logical sum of <tt>conditions</tt>.
     *
     * <p>
     * The resulting condition will have interval of the first condition
     * in the <tt>conditions</tt> array,
     *
     * @param conditions conditions to sum
     * @return logical sum of <tt>conditions</tt>
     * @throws IllegalArgumentException if <tt>conditions</tt> array is <code>null</code> or empty
     *
     * @see TimedCondition#interval()
     */
    @Nonnull
    public static CombinableCondition or(@Nonnull Iterable<TimedQuery<Boolean>> conditions)
    {
        return or(Iterables.toArray(conditions, TimedQuery.class));
    }

    /**
     * <p>
     * Returns a condition that combines <tt>original</tt> and <tt>dependant</tt> in a manner that dependant condition
     * will only ever be retrieved if the <tt>original</tt> condition is <code>true</code>. This is useful
     * when dependant condition may only be retrieved given the original condition is <code>true</code>.
     * </p>
     *
     * <p>
     * The supplier for dependant condition is allowed to return <code>null</code> or throw exception if the
     * original condition returns false. But it <i>may not</i> do so given the original condition is <code>true</code>,
     * as this will lead to <code>NullPointerException</code> or the raised exception be propagated by
     * this condition respectively.
     * </p>
     *
     * @param original original condition
     * @param dependant supplier for dependant condition that will only be evaluated given the original condition
     * evaluates to <code>true</code>
     * @return new dependant condition
     */
    @Nonnull
    public static TimedCondition dependantCondition(@Nonnull TimedQuery<Boolean> original,
                                                    @Nonnull Supplier<TimedQuery<Boolean>> dependant)
    {
        return new DependantCondition(original, dependant);
    }

    /**
     * <p/>
     * Return condition that will be <code>true</code>, if given <tt>matcher</tt> will match the <tt>query</tt>. Any
     * Hamcrest matcher implementation may be used.
     * <p/>
     * Example:<br>
     *
     * <code>
     *     TimedCondition textEquals = Conditions.forMatcher(element.getText(), isEqualTo("blah"));
     * </code>
     *
     * @param query timed query to match
     * @param matcher matcher for the query
     * @param <T> type of the result
     * @return new matching condition
     */
    @Nonnull
    public static <T> TimedCondition forMatcher(@Nonnull TimedQuery<T> query, @Nonnull Matcher<? super T> matcher)
    {
        return new MatchingCondition<T>(query, matcher);
    }

    /**
     * Returns timed condition verifying that given query will evaluate to value equal to <tt>value</tt>. The timeouts
     * are inherited from the provided <tt>query</tt>
     *
     * @param value value that <tt>query</tt> should be equalt to
     * @param query the timed query
     * @param <T> type of the value
     * @return timed condition for query equality to value
     */
    @Nonnull
    public static <T> TimedCondition isEqual(@Nullable T value, @Nonnull TimedQuery<T> query)
    {
        return forMatcher(query, equalTo(value));
    }

    /**
     * Returns a timed condition, whose current evaluation is based on a value provided by given <tt>supplier</tt>.
     *
     * @param supplier supplier of the current condition value
     * @return new query based on supplier
     */
    @Nonnull
    public static TimedCondition forSupplier(@Nonnull final Supplier<Boolean> supplier)
    {
        return forSupplier(DEFAULT_TIMEOUT, supplier);
    }

    /**
     * Returns a timed condition, whose current evaluation is based on a value provided by given <tt>supplier</tt>.
     *
     * @param defaultTimeout default timeout of the condition
     * @param supplier supplier of the current condition value
     * @return new query based on supplier
     */
    @Nonnull
    public static TimedCondition forSupplier(long defaultTimeout, @Nonnull final Supplier<Boolean> supplier)
    {
        return new AbstractTimedCondition(defaultTimeout, PollingQuery.DEFAULT_INTERVAL) {
            @Override
            protected Boolean currentValue() {
                return supplier.get();
            }
        };
    }

    /**
     * Returns a timed condition, whose current evaluation is based on a value provided by the {@code supplier} and with
     * a {@link TimeoutType#DEFAULT default timeout}.
     *
     * @param timeouts an instance of timeouts to use for the new condition
     * @param supplier supplier of the current condition value
     * @return new query based on supplier
     */
    @Nonnull
    public static TimedCondition forSupplier(@Nonnull Timeouts timeouts, @Nonnull final Supplier<Boolean> supplier)
    {
        return forSupplier(timeouts, supplier, TimeoutType.DEFAULT);
    }

    /**
     * Returns a timed condition, whose current evaluation is based on a value provided by the {@code supplier}, with
     * timeout of {@code timeoutType}.
     *
     * @param timeouts an instance of timeouts to use for the new condition
     * @param supplier supplier of the current condition value
     * @param timeoutType @param timeoutType timeout type for the condition
     * @return new timed condition based on supplier
     */
    @Nonnull
    public static TimedCondition forSupplier(@Nonnull Timeouts timeouts, @Nonnull final Supplier<Boolean> supplier,
                                             @Nonnull TimeoutType timeoutType)
    {
        checkNotNull(timeouts, "timeouts");
        checkNotNull(supplier, "supplier");
        checkNotNull(timeoutType, "timeoutType");

        return new AbstractTimedCondition(timeouts.timeoutFor(timeoutType),
                timeouts.timeoutFor(TimeoutType.EVALUATION_INTERVAL)) {
            @Override
            protected Boolean currentValue() {
                return supplier.get();
            }
        };
    }

    /**
     * A timed condition that always returns <code>true</code>
     *
     * @return timed condition that always returns true
     */
    @Nonnull
    public static TimedCondition alwaysTrue()
    {
        return new StaticCondition(true);
    }

    /**
     * A timed condition that always returns <code>false</code>
     *
     * @return timed condition that always returns false
     */
    @Nonnull
    public static TimedCondition alwaysFalse()
    {
        return new StaticCondition(false);
    }

    private static AbstractConditionDecorator asDecorator(TimedQuery<Boolean> condition)
    {
        return (AbstractConditionDecorator) condition;
    }

    private static class StaticCondition extends AbstractTimedCondition
    {
        private final Boolean value;

        public StaticCondition(Boolean value)
        {
            super(DEFAULT_TIMEOUT, DEFAULT_INTERVAL);
            this.value = checkNotNull(value);
        }

        @Override
        protected Boolean currentValue()
        {
            return value;
        }
    }

    /**
     * A timed condition that may be logically combined with others, by means of basic logical operations: 'and'/'or'. 
     *
     */
    public static interface CombinableCondition extends TimedCondition
    {
        /**
         * Combine <tt>other</tt> condition with this condition logical query, such that the resulting condition
         * represents a logical product of this condition and <tt>other</tt>.
         *
         * @param other condition to combine with this one
         * @return new combined 'and' condition
         */
        CombinableCondition and(TimedCondition other);

        /**
         * Combine <tt>other</tt> condition with this condition logical query, such that the resulting condition
         * represents a logical sum of this condition and <tt>other</tt>.
         *
         * @param other condition to combine with this one
         * @return new combined 'or' condition
         */
        CombinableCondition or(TimedCondition other);
    }

    private abstract static class AbstractConditionDecorator extends AbstractTimedCondition
    {
        protected final TimedQuery<Boolean> wrapped;

        public AbstractConditionDecorator(TimedQuery<Boolean> wrapped)
        {
            super(wrapped);
            this.wrapped = checkNotNull(wrapped, "wrapped");
        }
    }

    private abstract static class AbstractConditionsDecorator extends AbstractTimedCondition implements CombinableCondition
    {
        protected final TimedQuery<Boolean>[] conditions;

        public AbstractConditionsDecorator(TimedQuery<Boolean>... conditions)
        {
            super(conditions[0]);
            this.conditions = conditions;
        }

        @Override
        public String toString()
        {
            StringBuilder answer = new StringBuilder(conditions.length * 20).append(getClass().getName()).append(":\n");
            for (TimedQuery<Boolean> condition : conditions)
            {
                answer.append(" -").append(condition.toString()).append('\n');
            }
            return answer.deleteCharAt(answer.length()-1).toString();
        }
    }

    private static class Not extends AbstractConditionDecorator
    {
        public Not(TimedQuery<Boolean> other)
        {
            super(other);
        }

        public Boolean currentValue()
        {
            return !wrapped.now();
        }

        @Override
        public String toString()
        {
            return asString("Negated: <", wrapped, ">");
        }
    }

    private static class And extends AbstractConditionsDecorator
    {
        public And(TimedQuery<Boolean>... conditions)
        {
            super(conditions);
        }

        And(TimedQuery<Boolean>[] somes, TimedQuery<Boolean>[] more)
        {
            super((TimedCondition[]) ArrayUtils.addAll(somes, more));
        }

        And(TimedQuery<Boolean>[] somes, TimedQuery<Boolean> oneMore)
        {
            super((TimedCondition[]) ArrayUtils.add(somes, oneMore));
        }

        public Boolean currentValue()
        {
            boolean result = true;
            for (TimedQuery<Boolean> condition : conditions)
            {
                // null should not really happen if TimedCondition contract is observed
                result = condition.now() != null ? condition.now() : false;
                if (!result)
                {
                    // short-circuit
                    log.debug(asString("[And] Condition <", condition, "> returned false"));
                    break;
                }
            }
            return result;
        }

        public CombinableCondition and(TimedCondition other)
        {
            if (other.getClass().equals(And.class))
            {
                return new And(this.conditions, ((And) other).conditions);
            }
            return new And(this.conditions, other);
        }

        public CombinableCondition or(TimedCondition other)
        {
            if (other instanceof Or)
            {
                return ((Or)other).or(this);
            }
            return new Or(this, other);
        }
    }

    private static class Or extends AbstractConditionsDecorator
    {
        public Or(TimedQuery<Boolean>... conditions)
        {
            super(conditions);
        }

        Or(TimedQuery<Boolean>[] somes, TimedQuery<Boolean>[] more)
        {
            super((TimedCondition[]) ArrayUtils.addAll(somes, more));
        }

        Or(TimedQuery<Boolean>[] somes, TimedQuery<Boolean> oneMore)
        {
            super((TimedCondition[]) ArrayUtils.add(somes, oneMore));
        }

        public Boolean currentValue()
        {
            boolean result = false;
            for (TimedQuery<Boolean> condition : conditions)
            {
                // null should not really happen if TimedCondition contract is observed
                result = condition.now() != null ? condition.now() : false;
                if (result)
                {
                    // short-circuit
                    break;
                }
                log.debug(asString("[Or] Condition <", condition, "> returned false"));
            }
            return result;
        }

        public CombinableCondition and(TimedCondition other)
        {
            if (other instanceof And)
            {
                return ((And)other).and(this);
            }
            return new And(this, other);
        }

        public CombinableCondition or(TimedCondition other)
        {
            if (other.getClass().equals(Or.class))
            {
                return new Or(this.conditions, ((Or) other).conditions);
            }
            return new Or(this.conditions, other);
        }
    }

    private static final class DependantCondition extends AbstractConditionDecorator
    {
        private final Supplier<TimedQuery<Boolean>> dependant;

        DependantCondition(TimedQuery<Boolean> original, Supplier<TimedQuery<Boolean>> dependant)
        {
            super(original);
            this.dependant = checkNotNull(dependant, "dependant");
        }

        @Override
        public Boolean currentValue()
        {
            return wrapped.now() && dependant.get().now();
        }

        @Override
        public String toString()
        {
            if (wrapped.now())
            {
                TimedQuery<Boolean> dep = dependant.get();
                return asString("DependantCondition[original=",wrapped,",dependant=",dep,"]");
            }
            return asString("DependantCondition[original=",wrapped,"]");
        }
    }


    static final class MatchingCondition<T> extends AbstractTimedCondition
    {
        final TimedQuery<T> query;
        final Matcher<? super T> matcher;

        T lastValue;

        public MatchingCondition(final TimedQuery<T> query, final Matcher<? super T> matcher)
        {
            super(query);
            this.query = checkNotNull(query);
            this.matcher = checkNotNull(matcher);
        }

        @Override
        protected Boolean currentValue()
        {
            try
            {
                lastValue = query.now();
                return matcher.matches(lastValue);
            }
            catch (Exception e)
            {
                log.debug(String.format("TimedQuery.now() threw an exception. Not a match for %s", matcher), e);
                return false;
            }
        }

        @Override
        public String toString()
        {
            return super.toString() + new StringDescription().appendText("[query=").appendValue(query)
                    .appendText("][matcher=").appendDescriptionOf(matcher).appendText("]");
        }
    }
}
