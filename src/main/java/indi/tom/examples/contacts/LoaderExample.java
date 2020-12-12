package indi.tom.examples.contacts;

/**
 * @Author Tom
 * @Date 2020/12/12 21:54
 * @Version 1.0
 * @Description
 */
import indi.tom.examples.pof.Address;
import indi.tom.examples.pof.Contact;
import indi.tom.examples.pof.ContactId;
import indi.tom.examples.pof.PhoneNumber;
import com.tangosol.net.NamedCache;
import com.tangosol.net.Session;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

import static indi.tom.examples.contacts.ExamplesHelper.log;
import static indi.tom.examples.contacts.ExamplesHelper.logHeader;
import static com.tangosol.net.cache.TypeAssertion.withoutTypeChecking;

/**
 * LoaderExample loads contacts into the cache from a file or stream.
 * <p/>
 * Demonstrates the most effective way of inserting data into a cache using the
 * Map.putAll() method. This will allow for minimizing the number of network
 * roundtrips between the application and the cache.
 *
 * @author dag  2009.02.20
 */
public class LoaderExample
{
    // ----- static methods -------------------------------------------------

    /**
     * Load contacts from a CSV file, then populate the cache with the data.
     * <p/>
     * The first argument is the name of the datafile to load. The second
     * argument will be treated as the name of the cache to populate.
     * <p/>
     * usage: [file name] [cache name]
     *
     * @param asArg  command line arguments
     *
     * @throws IOException if file cannot be read
     */
    public static void main(String[] asArg)
            throws IOException
    {
        String sFile  = asArg.length > 0 ? asArg[0] : Driver.DEFAULT_DATAFILE;
        String sCache = asArg.length > 1 ? asArg[1] : CACHENAME;

        System.out.println("input file: " + sFile);
        System.out.println("cache name: " + sCache);

        try (Session session = Session.create())
        {
            new LoaderExample().load(new FileInputStream(sFile),
                    session.getCache(sCache, withoutTypeChecking()));
        }
        catch (Exception e)
        {
            log("Error running loader example");
            e.printStackTrace();
        }
    }

    /**
     * Load contacts from the inputstream and insert them into the cache.
     *
     * @param in     stream containing contacts
     * @param cache  target cache
     *
     * @throws IOException on read error
     */
    public void load(InputStream in, NamedCache<ContactId, Contact> cache)
            throws IOException
    {
        BufferedReader          reader    = new BufferedReader(new InputStreamReader(in));
        Map<ContactId, Contact> mapBatch  = new HashMap<ContactId, Contact>(BATCH_SIZE);
        int                     cContacts = 0;
        Contact                 contact;

        logHeader("LoaderExample begins");
        while ((contact = readContact(reader)) != null)
        {
            mapBatch.put(new ContactId(contact.getFirstName(),
                    contact.getLastName()), contact);
            ++cContacts;

            // When reached the BATCH_SIZE threashold transfer the records to
            // the cache.
            if (cContacts % BATCH_SIZE == 0)
            {
                // minimize the network roundtrips by using putAll()
                cache.putAll(mapBatch);
                mapBatch.clear();
                System.out.print('.');
                System.out.flush();
            }
        }

        // insert the final batch
        if (!mapBatch.isEmpty())
        {
            cache.putAll(mapBatch);
        }

        System.out.println("Added " + cContacts + " entries to cache");
        logHeader("LoaderExample completed");
    }

    /**
     * Read a single contact from the supplied stream.
     *
     * @param reader  the stream from which to read a contact
     *
     * @return the contact or null upon reaching end of stream
     *
     * @throws IOException  on read error
     */
    public Contact readContact(BufferedReader reader)
            throws IOException
    {
        String sRecord = reader.readLine();
        if (sRecord == null)
        {
            return null;
        }

        String[] asPart     = sRecord.split(",");
        int      ofPart     = 0;
        String   sFirstName = asPart[ofPart++];
        String   sLastName  = asPart[ofPart++];
        String[] asDate     = asPart[ofPart++].split("-");

        LocalDate dtBirth = LocalDate.of(Integer.parseInt(asDate[0]),
                Integer.parseInt(asDate[1]),
                Integer.parseInt(asDate[2]));

        Address                  addrHome   = new Address(
                /*streetline1*/ asPart[ofPart++],
                /*streetline2*/ asPart[ofPart++],
                /*city*/        asPart[ofPart++],
                /*state*/       asPart[ofPart++],
                /*zip*/         asPart[ofPart++],
                /*country*/     asPart[ofPart++]);
        Address                  addrWork   = new Address(
                /*streetline1*/ asPart[ofPart++],
                /*streetline2*/ asPart[ofPart++],
                /*city*/        asPart[ofPart++],
                /*state*/       asPart[ofPart++],
                /*zip*/         asPart[ofPart++],
                /*country*/     asPart[ofPart++]);
        Map<String, PhoneNumber> mapTelNum  = new HashMap<String, PhoneNumber>();

        for (int c = asPart.length; ofPart < c; )
        {
            mapTelNum.put(/*type*/ asPart[ofPart++],
                    new PhoneNumber(
                            /*access code*/  Short.parseShort(asPart[ofPart++]),
                            /*country code*/ Short.parseShort(asPart[ofPart++]),
                            /*area code*/    Short.parseShort(asPart[ofPart++]),
                            /*local num*/    Long.parseLong(asPart[ofPart++])));
        }

        return  new Contact(sFirstName, sLastName, addrHome,
                addrWork, mapTelNum, dtBirth);
    }


    // ----- constants ------------------------------------------------------

    /**
     * Default cache name.
     */
    public static final String CACHENAME = "contacts";

    /**
     * The maximum number of contacts to load at a time.
     */
    private static final int BATCH_SIZE = 1024;
}
