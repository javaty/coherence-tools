using System;
using System.IO;
using Seovic.Loader;
using Seovic.Loader.Source;
using Spring.Core.IO;
using Tangosol.Net;

namespace Seovic.Coherence.Loader
{
    /// <summary>
    /// Convenience class that loads data from the CSV file into Coherence cache
    /// using default settings.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.09.29</author>
    /// <author>Ivan Cikic  2009.10.31</author>
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

        /// <summary>
        /// Main method.
        /// </summary>
        /// <param name="args">Command line arguments.</param>
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
