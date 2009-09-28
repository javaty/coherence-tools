namespace Seovic.Coherence.Core
{
    public interface IEntity<TId>
    {
        TId Id
        {
            get;
        }
    }
}
