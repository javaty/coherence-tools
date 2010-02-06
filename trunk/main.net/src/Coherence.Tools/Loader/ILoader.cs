
namespace Seovic.Loader
{
    /// <summary>
    /// An interface that all loaders have to implement.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.09.29</author>
    public interface ILoader
    {
        /// <summary>
        /// Load items from the ISource into the ITarget.
        /// </summary>
        void Load();
    }
}
