package indi.tom.examples.pof;

/**
 * @Author Tom
 * @Date 2020/12/12 21:33
 * @Version 1.0
 * @Description
 */
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;

/**
 * PhoneNumber represents a sequence of numers used to call a telephone.
 * <p/>
 * An example that uses the full sequence of numbers is a call from the United
 * States to Beijing, China: 011 86 10 85001234.
 * <p/>
 * The type implements PortableObject for efficient cross-platform
 * serialization..
 *
 * @author dag  2009.02.17
 */
public class PhoneNumber
        implements PortableObject
{
    // ----- constructors ---------------------------------------------------

    /**
     * Default constructor (necessary for PortableObject implementation).
     */
    public PhoneNumber()
    {
    }

    /**
     * Construct a Phone.
     *
     * @param nAccessCode   the numbers used to access international or
     *                      non-local calls
     * @param nCountryCode  the numbers used to designate a country
     * @param nAreaCode     the numbers used to indicate a geographical region
     * @param lLocalNumber  the local numbers
     */
    public PhoneNumber(short nAccessCode, short nCountryCode,
                       short nAreaCode, long lLocalNumber)
    {
        m_nAccessCode  = nAccessCode;
        m_nCountryCode = nCountryCode;
        m_nAreaCode    = nAreaCode;
        m_lLocalNumber = lLocalNumber;
    }

    // ----- accessors ------------------------------------------------------

    /**
     * Return the access code.
     *
     * @return the access code
     */
    public short getAccessCode()
    {
        return m_nAccessCode;
    }

    /**
     * Set the numbers used to access international or non-local calls.
     *
     * @param nAccessCode  the access code numbers
     */
    public void setAccessCode(short nAccessCode)
    {
        m_nAccessCode = nAccessCode;
    }

    /**
     * Return the country code.
     *
     * @return the country code
     */
    public short getCountryCode()
    {
        return m_nCountryCode;
    }

    /**
     * Set the country code.
     *
     * @param nCountryCode  the country code
     */
    public void setCountryCode(short nCountryCode)
    {
        m_nCountryCode = nCountryCode;
    }

    /**
     * Return the area code.
     *
     * @return the area code
     */
    public short getAreaCode()
    {
        return m_nAreaCode;
    }

    /**
     * Set the numbers used indicate a geographic area within a country.
     *
     * @param nAreaCode  the area code
     */
    public void setAreaCode(short nAreaCode)
    {
        m_nAreaCode = nAreaCode;
    }

    /**
     * Return the local or subscriber number.
     *
     * @return the local or subscriber number
     */
    public long getLocalNumber()
    {
        return m_lLocalNumber;
    }

    /**
     * Set the local or subscriber number.
     *
     * @param lLocalNumbeer  the local or subscriber number
     */
    public void setLocalNumber(long lLocalNumbeer)
    {
        m_lLocalNumber = lLocalNumbeer;
    }

    // ----- PortableObject interface ---------------------------------------

    /**
     * {@inheritDoc}
     */
    public void readExternal(PofReader reader)
            throws IOException
    {
        m_nAccessCode  = reader.readShort(ACCESS_CODE);
        m_nCountryCode = reader.readShort(COUNTRY_CODE);
        m_nAreaCode    = reader.readShort(AREA_CODE);
        m_lLocalNumber = reader.readLong(LOCAL_NUMBER);
    }

    /**
     * {@inheritDoc}
     */
    public void writeExternal(PofWriter writer)
            throws IOException
    {
        writer.writeShort(ACCESS_CODE, m_nAccessCode);
        writer.writeShort(COUNTRY_CODE, m_nCountryCode);
        writer.writeShort(AREA_CODE, m_nAreaCode);
        writer.writeLong(LOCAL_NUMBER, m_lLocalNumber);
    }


    // ----- Object methods -------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object oThat)
    {
        if (this == oThat)
        {
            return true;
        }
        if (oThat == null)
        {
            return false;
        }


        PhoneNumber that = (PhoneNumber) oThat;
        return getAccessCode()  == that.getAccessCode()  &&
                getCountryCode() == that.getCountryCode() &&
                getAreaCode()    == that.getAreaCode()    &&
                getLocalNumber() == that.getLocalNumber();
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        return (int) ((long) getAreaCode() * 31L + getLocalNumber());
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "+" + getAccessCode() + " " + getCountryCode() + " "
                + getAreaCode()   + " " + getLocalNumber();
    }

    // ----- constants -------------------------------------------------------

    /**
     * The POF index for the AccessCode property.
     */
    public static final int ACCESS_CODE = 0;

    /**
     * The POF index for the CountryCode property.
     */
    public static final int COUNTRY_CODE = 1;

    /**
     * The POF index for the AreaCode property.
     */
    public static final int AREA_CODE = 2;

    /**
     * The POF index for the LocalNumber property.
     */
    public static final int LOCAL_NUMBER = 3;

    // ----- data members ---------------------------------------------------

    /**
     * The numbers used to access international or non-local calls.
     */
    private short m_nAccessCode;

    /**
     * The numbers used to designate a country in international calls.
     */
    private short m_nCountryCode;

    /**
     * The numbers used indicate a geographic area within a country.
     */
    private short m_nAreaCode;

    /**
     * The local number.
     */
    private long m_lLocalNumber;
}