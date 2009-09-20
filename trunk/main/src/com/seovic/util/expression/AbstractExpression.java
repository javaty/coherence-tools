package com.seovic.util.expression;


import com.seovic.util.Expression;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import java.io.Serializable;
import java.io.IOException;


/**
 * Abstract base class for various expression implementations.
 * <p/>
 * This class takes care of expression serialization and overrides Object
 * methods.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
public abstract class AbstractExpression
        implements Expression, Serializable, PortableObject
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public AbstractExpression()
        {
        }

    /**
     * Construct an expression instance.
     *
     * @param expression  the expression to evaluate
     */
    public AbstractExpression(String expression)
        {
        m_expression = expression;
        }


    // ---- Expression implementation ---------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object eval(Object target)
        {
        return eval(target, null);
        }

    // ---- PortableObject implementation -----------------------------------

    /**
     * Deserialize this object from a POF stream.
     *
     * @param reader  POF reader to use
     *
     * @throws IOException  if an error occurs during deserialization
     */
    public void readExternal(PofReader reader)
            throws IOException
        {
        m_expression = reader.readString(0);
        }

    /**
     * Serialize this object into a POF stream.
     *
     * @param writer  POF writer to use
     *
     * @throws IOException  if an error occurs during serialization
     */
    public void writeExternal(PofWriter writer)
            throws IOException
        {
        writer.writeString(0, m_expression);
        }


    // ---- Object methods --------------------------------------------------

    /**
     * Test objects for equality.
     *
     * @param o  object to compare this object with
     *
     * @return <tt>true</tt> if the specified object is equal to this object
     *         <tt>false</tt> otherwise
     */
    @Override
    public boolean equals(Object o)
        {
        if (this == o)
            {
            return true;
            }
        if (o == null || getClass() != o.getClass())
            {
            return false;
            }

        AbstractExpression that = (AbstractExpression) o;
        return m_expression.equals(that.m_expression);
        }

    /**
     * Return hash code for this object.
     *
     * @return this object's hash code
     */
    @Override
    public int hashCode()
        {
        return m_expression.hashCode();
        }

    /**
     * Return string representation of this object.
     *
     * @return string representation of this object
     */
    @Override
    public String toString()
        {
        return getClass().getSimpleName() + "{" +
               "expression='" + m_expression + '\'' +
               '}';
        }


    // ---- data members ----------------------------------------------------

    /**
     * Expression source.
     */
    protected String m_expression;
    }
