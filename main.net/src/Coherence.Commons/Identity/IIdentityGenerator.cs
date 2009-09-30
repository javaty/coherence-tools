
namespace Seovic.Coherence.Identity
{
    /// <summary>
    /// A strategy interface for identity generators.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.05.27</author>
    /// <author>Ivan Cikic  2009.09.29</author>
    public interface IIdentityGenerator<T>
    {
        /// <summary>
        /// Generates new identity instance.
        /// </summary>
        /// <returns>New identity instance</returns>
        T GenerateIdentity();
    }

    /// <summary>
    /// A strategy interface for identity generators.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.05.27</author>
    /// <author>Ivan Cikic  2009.09.29</author>
    public interface IIdentityGenerator : IIdentityGenerator<object>
    {
    }
}