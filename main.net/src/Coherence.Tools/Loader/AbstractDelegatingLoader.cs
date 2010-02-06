using System.IO;
using Spring.Core.IO;

namespace Seovic.Loader
{
    /// <summary>
    /// Base class for delegating loaders.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.09.29</author>
    /// <author>Ivan Cikic  2009.10.01</author>
    public class AbstractDelegatingLoader : ILoader
    {
        #region Properties

        /// <summary>
        /// Return the loader processing should be delegated to.
        /// </summary>
        public ILoader Loader
        {
            get { return m_loader; }
            set { m_loader = value; }
        }

        #endregion

        #region ILoader implementation

        /// <summary>
        /// Load items from the ISource into the ITarget.
        /// </summary>
        public void Load()
        {
            m_loader.Load();
        }

        #endregion

        #region Helper methods

        /// <summary>
        /// Return a IResource represented by the specified location.
        /// </summary>
        /// <param name="location">Resource location</param>
        /// <returns>A resource.</returns>
        protected static IResource GetResource(string location)
        {
            return new ConfigurableResourceLoader().GetResource(location);
        }

        /// <summary>
        /// Create a reader for the specified resource.
        /// </summary>
        /// <param name="resource">Resource to create reader for</param>
        /// <returns>Reader for the specified resource</returns>
        protected static TextReader CreateResourceReader(IResource resource)
        {
            return new StreamReader(resource.InputStream);
        }

        #endregion

        #region Data members

        /// <summary>
        /// The loader processing should be delegated to.
        /// </summary>
        private ILoader m_loader;

        #endregion
    }
}
