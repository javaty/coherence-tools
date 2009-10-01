using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using Seovic.Coherence.Loader.Source;
using Seovic.Coherence.Loader.Target;
using Spring.Core.IO;
using Tangosol.Net;

namespace Seovic.Coherence.Loader
{
    public class CsvToCoherence : AbstractDelegatingLoader
    {

        #region Constructors

        /// <summary>
        /// Construct CsvToCoherence loader instance.
        /// </summary>
        /// <param name="csvFile">CSV file to read items from</param>
        /// <param name="cache">Coherence cache to import objects into</param>
        /// <param name="itemType">target item class</param>
        public CsvToCoherence(String csvFile, INamedCache cache, Type itemType) 
            : this(GetResource(csvFile), cache, itemType)
        {
        }

        /// <summary>
        /// Construct CsvToCoherence loader instance.
        /// </summary>
        /// <param name="csvFile">CSV file to read items from</param>
        /// <param name="cache">Coherence cache to import objects into</param>
        /// <param name="itemType">target item class</param>
        public CsvToCoherence(IResource csvFile, INamedCache cache, Type itemType) 
            : this(CreateResourceReader(csvFile), cache, itemType)
        {
        }

        /// <summary>
        /// Construct CsvToCoherence loader instance.
        /// </summary>
        /// <param name="csvReader">CSV file reader</param>
        /// <param name="cache">Coherence cache to import objects into</param>
        /// <param name="itemType">target item class</param>
        public CsvToCoherence(TextReader csvReader, INamedCache cache, Type itemType)
        {
            ISource source = new CsvSource(csvReader);
            ITarget target = new CoherenceCacheTarget(cache, itemType);
            Loader = new DefaultLoader(source, target);
        }


        #endregion

        #region Main method

        public static void Main(string[] args)
        {
            if (args.Length < 3)
            {
                Console.WriteLine("Usage: CsvToCoherence <csvFile> <cacheName> <itemClass>");
            }

            TextReader  csvReader = new StreamReader(args[0]);
            INamedCache cache     = CacheFactory.GetCache(args[1]);
            Type        itemType  = Type.GetType(args[2]);

            new CsvToCoherence(csvReader, cache, itemType).Load();
        }

        #endregion
    }
}
