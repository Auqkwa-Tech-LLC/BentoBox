package world.bentobox.bentobox.api.addons.exception;

/**
 * @deprecated Moved to {@link world.bentobox.bentobox.api.addons.exceptions.InvalidAddonInheritException}.
 */
@Deprecated
public class InvalidAddonInheritException extends AddonException {

    /**
     *
     */
    private static final long serialVersionUID = -5847358994397613244L;

    public InvalidAddonInheritException(String errorMessage) {
        super(errorMessage);
    }

}