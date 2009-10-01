using System.IO;
using Spring.Core.IO;

namespace Seovic.Coherence.Loader
{
    public class AbstractDelegatingLoader : ILoader
    {
        #region Properties

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

        protected static IResource GetResource(string location)
        {
            return new ConfigurableResourceLoader().GetResource(location);
        }

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
